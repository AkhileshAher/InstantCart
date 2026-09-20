package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.OrderEmailData;
import in.akhilesh.instantcart.dto.admin.AdminOrderResponse;
import in.akhilesh.instantcart.dto.delivery.DeliveryPartnerResponse;
import in.akhilesh.instantcart.dto.order.*;
import in.akhilesh.instantcart.dto.user.UserResponse;
import in.akhilesh.instantcart.entity.*;
import in.akhilesh.instantcart.entity.enums.OrderStatus;
import in.akhilesh.instantcart.repository.DeliveryPartnerRepository;
import in.akhilesh.instantcart.repository.OrderRepository;
import in.akhilesh.instantcart.repository.ProductRepository;
import in.akhilesh.instantcart.repository.UserRepository;
import in.akhilesh.instantcart.utils.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final EmailService emailService;


    @Transactional
    @CacheEvict(
            value = "userOrders",
            allEntries = true
    )
    @PreAuthorize(value = "hasRole('CUSTOMER') and #userId == authentication.principal.userId")
    public OrderResponse createOrder(ObjectId userId, CreateOrderRequest request, boolean isPaid) {

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be empty");
        }

        if (request.getShippingAddress() == null) {
            throw new IllegalArgumentException("Shipping address cannot be empty");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Order order = new Order();
        order.setUserId(user.getId());
        order.setUser(mapUserToResponse(user));

        // ITEMS

        List<OrderItem> orderItems = new ArrayList<>();

        double subTotal = 0;

        for (OrderItemRequest item : request.getItems()) {

            ObjectId productId = item.getProductId();
            Integer requestedQuantity = item.getQuantity();
            if (productId == null) {
                throw new IllegalArgumentException("Product ID cannot be null");
            }
            if (requestedQuantity == null || requestedQuantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }
            Product product = productRepo.findById(productId)
                            .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

            if (product.getStock() < requestedQuantity) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(productId.toHexString());
            orderItem.setAvatar(product.getImage());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(requestedQuantity);
            orderItems.add(orderItem);

            subTotal += product.getPrice() * requestedQuantity;

            product.setStock(product.getStock() - requestedQuantity);
            productRepo.save(product);
        }
        order.setItems(orderItems);

        // SHIPPING ADDRESS
        ShippingAddressRequest addressRequest = request.getShippingAddress();

        ShippingAddress address = new ShippingAddress();
        address.setLabel(addressRequest.getLabel());
        address.setAddress(addressRequest.getAddress());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setZip(addressRequest.getZip());
        address.setLat(addressRequest.getLat());
        address.setLng(addressRequest.getLng());
        order.setShippingAddress(address);

        // PAYMENT
        String paymentMethod = request.getPaymentMethod();

        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new IllegalArgumentException("Payment method is required");
        }

        if (!paymentMethod.equalsIgnoreCase("card")
                && !paymentMethod.equalsIgnoreCase("cod")) {

            throw new IllegalArgumentException("Invalid payment method");
        }

        order.setPaymentMethod(paymentMethod.toLowerCase());

        double deliveryFee = subTotal >= 200 ? 0.0 : 40.0;
        double tax = subTotal * 0.05;
        double total = subTotal + deliveryFee + tax;

        order.setSubTotal(subTotal);
        order.setDeliveryFee(deliveryFee);
        order.setTax(tax);
        order.setTotal(total);
        order.setStatus(OrderStatus.PLACED);

        List<OrderStatusHistory> statusHistory = new ArrayList<>();
        OrderStatusHistory history = new OrderStatusHistory();
        history.setStatus(OrderStatus.PLACED);
        history.setChangedAt(LocalDateTime.now());
        statusHistory.add(history);

        order.setStatusHistory(statusHistory);
        order.setIsPaid(isPaid);
        order.setDeliveryOtp("");

        Order saved = orderRepo.save(order);
        return mapOrderToResponse(saved,userId);
    }

    @Cacheable(
            value = "userOrders",
            key = "#userId.toHexString() + ':' + " +
                    "T(java.util.Objects).toString(#status, 'ALL')"
    )
    @PreAuthorize(value = "hasRole('CUSTOMER') and #userId == authentication.principal.userId")
    public List<OrderResponse> getUserOrders(ObjectId userId, OrderStatus status) {

        if (status == null) {
            return orderRepo.findByUserId(userId).stream()
                    .map((order) -> mapOrderToResponse(order,userId))
                    .toList();
        }

        return orderRepo.findByUserIdAndStatus(userId, status).stream()
                .map((order) -> mapOrderToResponse(order,userId))
                .toList();
    }


    @PreAuthorize(value = "hasRole('CUSTOMER') and #userId == authentication.principal.userId")
    public OrderResponse getOrder(ObjectId orderId, ObjectId userId) {

        Order order = orderRepo.findByIdAndUserId(orderId,userId)
                        .orElse(null);

        if (order == null) {
            throw new RuntimeException("Order does not exist with this ID");
        }

        OrderResponse response = mapOrderToResponse(order,userId);

        return response;
    }

    @PreAuthorize(value = "hasRole('DELIVERY') and #userId == authentication.principal.userId ")
    public List<OrderResponse> getOrdersOfDeliveryPartner(ObjectId userId) {
        return orderRepo.findByDeliveryPartnerId(userId).stream()
                .map(order -> mapOrderToResponse(order,order.getUserId()))
                .toList();
    }

    @PreAuthorize(value = "hasRole('CUSTOMER') and #userId == authentication.principal.userId")
    public LiveLocation getOrderLocation(ObjectId orderId, ObjectId userId) {

        Order order = orderRepo.findById(orderId)
                        .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("You are not allowed to access this order");
        }

        return order.getLiveLocation();
    }
    
    @Transactional
    @PreAuthorize(value = "hasRole('VENDOR')")
    public OrderResponse changeOrderStatus(ObjectId orderId, OrderStatus status) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order does not exist with this id : " + orderId));

        if(status == OrderStatus.ASSIGNED) {
            throw new RuntimeException("Assign Order to Delivery Partner");
        }

        if(status == OrderStatus.OUT_FOR_DELIVERY || status == OrderStatus.DELIVERED || status == OrderStatus.CANCELLED) {
            throw new RuntimeException("Do Not have rights");
        }

        OrderStatus currentStatus = order.getStatus();

        if(!currentStatus.canTransitionTo(status)) {
            throw new RuntimeException("Invalid status change: " + currentStatus + " → " + status);
        }

        updateOrderStatus(order,status);
        orderRepo.save(order);
        return mapOrderToResponse(order,order.getUserId());
    }


    public List<OrderResponse> getOrdersOfDeliveryPartner(ObjectId deliveryPartnerId, String status) {

        List<Order> orders;

        if ("ACTIVE".equalsIgnoreCase(status)) {

            orders = orderRepo.findByDeliveryPartnerIdAndStatusIn(deliveryPartnerId,
                    List.of(OrderStatus.ASSIGNED, OrderStatus.OUT_FOR_DELIVERY));

        } else if ("DELIVERED".equalsIgnoreCase(status)) {

            orders = orderRepo.findByDeliveryPartnerIdAndStatus(deliveryPartnerId, OrderStatus.DELIVERED);

        } else {
            throw new IllegalArgumentException("Invalid delivery order status: " + status);
        }

        return orders.stream()
                .map(order -> mapOrderToResponse(order, deliveryPartnerId))
                .toList();
    }




    private void updateOrderStatus(Order order, OrderStatus status) {

        order.setStatus(status);

        List<OrderStatusHistory> historyList = order.getStatusHistory();
        if (historyList == null) {
            historyList = new ArrayList<>();
        }

        OrderStatusHistory history = new OrderStatusHistory();
        history.setStatus(status);
        history.setChangedAt(LocalDateTime.now());
        historyList.add(history);
        order.setStatusHistory(historyList);
    }

    @Transactional
    @PreAuthorize("hasRole('DELIVERY') and #deliveryPartnerId == authentication.principal.userId")
    public Order markOrderAsDelivered(ObjectId orderId, String otp, ObjectId deliveryPartnerId) {

        Order order = orderRepo.findById(orderId)
                        .orElseThrow(() -> new RuntimeException("Order does not exist with this ID"));

        if (order.getDeliveryPartnerId() == null) {
            throw new RuntimeException("No delivery partner assigned to this order");
        }

        if (!order.getDeliveryPartnerId().equals(deliveryPartnerId)) {
            throw new RuntimeException("You are not assigned to this order");
        }

        if (order.getDeliveryOtp() == null || !order.getDeliveryOtp().equals(otp)) {
            throw new RuntimeException("Invalid delivery OTP");
        }

        if (!OrderStatus.OUT_FOR_DELIVERY.equals(order.getStatus())) {
            throw new RuntimeException("Order is not out for delivery");
        }

        updateOrderStatus(order, OrderStatus.DELIVERED);
        order.setDeliveryOtp("");
        return orderRepo.save(order);
    }

    @PreAuthorize(value = "hasRole('VENDOR')")
    public List<AdminOrderResponse> getAllOrders() {

        List<Order> orders = orderRepo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

        return orders.stream().map(order -> {
                    User customer = userRepo.findById(order.getUserId()).orElse(null);
                    DeliveryPartner deliveryPartner = null;
                    if (order.getDeliveryPartnerId() != null) {
                        deliveryPartner = deliveryPartnerRepository.findById(order.getDeliveryPartnerId()).orElse(null);
                    }
                    return new AdminOrderResponse(
                            order.getId().toHexString(),
                            customer != null ? customer.getName() : null,
                            order.getTotal(),
                            order.getStatus(),
                            deliveryPartner != null ? deliveryPartner.getId() : null,
                            deliveryPartner != null ? deliveryPartner.getName() : null,
                            order.getCreatedAt()
                    );
                }).toList();
    }

    @Transactional
    @PreAuthorize(value = "hasRole('VENDOR')")
    public Order assignDeliveryPartner(ObjectId orderId, ObjectId deliveryPartnerId) {
        Order order = orderRepo.findById(orderId)
                        .orElseThrow(() -> new RuntimeException("Order does not exist with this ID"));

        DeliveryPartner deliveryPartner = deliveryPartnerRepository.findById(deliveryPartnerId)
                        .orElseThrow(() -> new RuntimeException("Delivery partner not found"));

        OrderStatus currentStatus = order.getStatus();
        if(!currentStatus.canTransitionTo(OrderStatus.ASSIGNED)) {
            throw new RuntimeException("Invalid status change: " + currentStatus + " → " + OrderStatus.ASSIGNED);
        }

        order.setStatus(OrderStatus.ASSIGNED);
        updateOrderStatus(order,OrderStatus.ASSIGNED);

        order.setDeliveryPartner(deliveryPartner);
        order.setDeliveryPartnerId(deliveryPartner.getId());

        Integer otp = OtpGenerator.getOtp();
        order.setDeliveryOtp(otp.toString());

        OrderEmailData emailData = OrderEmailData.builder()
                .otp(otp.toString())
                .orderId(order.getId().toString())
                .orderDate(order.getCreatedAt().toString())
                .orderStatus(order.getStatus().toString())
                .subtotal(order.getSubTotal())
                .deliveryFee(order.getDeliveryFee())
                .totalAmount(order.getTotal())
                .deliveryAddress(order.getShippingAddress().getAddress())
                .build();
        User user = userRepo.findById(order.getUserId()).orElse(null);
        // EXTERNALIZE MAIL SYSTEM
        if(user != null)
            emailService.sendMail(user.getEmail(), "YOUR Order Status",emailData);

        Order saved = orderRepo.save(order);
        return saved;
    }

    @Transactional
    @PreAuthorize(value = "hasRole('ADMIN')")
    public void deleteOrder(ObjectId userId,ObjectId orderId) {
        Order order = orderRepo.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Order does not exist with id : " + orderId));
        orderRepo.delete(order);
    }

    private OrderResponse mapOrderToResponse(Order order,ObjectId userId) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId().toHexString());
        response.setUserId(userId.toHexString());
        response.setUser(order.getUser());
        response.setItems(order.getItems());
        response.setShippingAddress(order.getShippingAddress());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setSubTotal(order.getSubTotal());
        response.setDeliveryFee(order.getDeliveryFee());
        response.setTax(order.getTax());
        response.setTotal(order.getTotal());
        response.setStatus(order.getStatus());
        response.setStatusHistory(order.getStatusHistory());
        response.setDeliveryPartnerId(order.getDeliveryPartnerId() != null ? order.getDeliveryPartnerId().toHexString() : "" );
    if(order.getDeliveryPartner() != null) {
        DeliveryPartner partner = order.getDeliveryPartner();
        DeliveryPartnerResponse res = new DeliveryPartnerResponse();
        res.setId(partner.getId().toHexString());
        res.setName(partner.getName());
        res.setEmail(partner.getEmail());
        res.setPhone(partner.getPhone());
        res.setAvatar(partner.getAvatar());
        res.setVehicleType(partner.getVehicleType());
        res.setIsActive(partner.getIsActive());
        res.setCreatedAt(partner.getCreatedAt());
        res.setUpdatedAt(partner.getUpdatedAt());
        response.setDeliveryPartner(res);
    }
        response.setDeliveryOtp(order.getDeliveryOtp() != null ? order.getDeliveryOtp() : "" );
        response.setLiveLocation(order.getLiveLocation() != null ? order.getLiveLocation() : null );
        response.setIsPaid(order.getIsPaid());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        return response;
    }

    // DELIVERY PARTNER ACCESS TO CHANGE STATUS TO DELIVERED CANCELLED AND OUT FOR DELIVERY
    @Transactional
    @PreAuthorize(value = "hasRole('DELIVERY')")
    public ResponseEntity<Order> changeStatus(ObjectId orderId, OrderStatus newStatus,ObjectId deliveryPartnerId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No Order Exist with this id: " + orderId));


        if (order.getDeliveryPartnerId() == null || !order.getDeliveryPartnerId().equals(deliveryPartnerId)) {
            throw new RuntimeException("You are not assigned to this order");
        }

        OrderStatus currentStatus = order.getStatus();

        // Same status
        if (currentStatus == newStatus) {
            throw new RuntimeException("Order is already in " + newStatus + " status");
        }

        if (currentStatus == OrderStatus.DELIVERED || currentStatus == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order is already " + currentStatus + " and its status cannot be changed");
        }

        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new RuntimeException("Invalid status change: " + currentStatus + " → " + newStatus);
        }

        if (newStatus != OrderStatus.OUT_FOR_DELIVERY && newStatus != OrderStatus.CANCELLED) {
            throw new RuntimeException("You don't have permission to change to this status");
        }

        if (newStatus == OrderStatus.DELIVERED) {
            throw new RuntimeException("Use OTP verification to mark the order as delivered");
        }

        order.setStatus(newStatus);
        updateOrderStatus(order, newStatus);
        Order updatedOrder = orderRepo.save(order);

        return ResponseEntity.ok(updatedOrder);
    }

    private UserResponse mapUserToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setAvatar(user.getAvatar());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    public LiveLocation updateLocation(LiveLocation location,ObjectId orderId,ObjectId deliveryPartnerId) {
        Order order = orderRepo.findByIdAndDeliveryPartnerId(orderId, deliveryPartnerId).orElse(null);
        if(order == null) {
            throw new RuntimeException("Order or DeliveryPartner Not Exist");
        }
        order.setLiveLocation(location);
        Order saved = orderRepo.save(order);
        return saved.getLiveLocation();

    }
}