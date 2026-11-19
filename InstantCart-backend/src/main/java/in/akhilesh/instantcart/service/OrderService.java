package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.OrderResponse;
import in.akhilesh.instantcart.dto.PlaceOrderRequest;
import in.akhilesh.instantcart.entity.*;
import in.akhilesh.instantcart.entity.OrderStatus;
import in.akhilesh.instantcart.entity.PaymentMethod;
import in.akhilesh.instantcart.entity.PaymentStatus;
import in.akhilesh.instantcart.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Every operation that changes an order lives here, so each one runs in a single transaction.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private static final double DELIVERY_FEE = 20.0;
    private static final double TAX_RATE = 0.05;
    private static final Set<OrderStatus> PARTNER_STATUSES =
            EnumSet.of(OrderStatus.PICKED_UP, OrderStatus.OUT_FOR_DELIVERY);

    private final OrderRepository orders;
    private final AddressRepository addresses;
    private final CartItemRepository carts;
    private final DeliveryPartnerRepository partners;

    // ---------------- customer ----------------

    public List<OrderResponse> getAllOrder(User user) {
        return orders.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream().map(o -> OrderResponse.from(o, true))
                .toList();
    }

    public OrderResponse getUserOrder(User user, Long id) {
        return orders.findByIdAndUserId(id, user.getId())
                .map(o -> OrderResponse.from(o, true))
                .orElseThrow(() -> new RuntimeException("Order Not Found"));
    }

    @Transactional
    public Order place(User user, PlaceOrderRequest req) {
        Address address = addresses.findByIdAndUserId(req.addressId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Address Not Found"));

        List<CartItem> cart = carts.findByUserId(user.getId());
        if (cart.isEmpty()) {
            throw new RuntimeException("Cart is Empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber("IC" + System.currentTimeMillis());
        order.setDeliveryOtp(String.format("%04d", new SecureRandom().nextInt(10000)));
        order.setPaymentMethod(req.paymentMethod());
        order.setShippingAddress(snapshotOf(address, user));

        for (CartItem c : cart) {
            Product p = c.getProduct();
            if (!p.getIsActive() || p.getStock() < c.getQuantity()) {
                throw new RuntimeException(p.getName() + " is not available in the requested quantity");
            }
            p.setStock(p.getStock() - c.getQuantity());
            order.addItem(OrderItem.from(p, c.getQuantity()));
        }

        double taxable = order.getItems().stream().mapToDouble(OrderItem::getLineTotal).sum();
        order.setDeliveryFee(DELIVERY_FEE);
        order.setTax(round(taxable * TAX_RATE));
        order.calculateTotals();
        order.addStatusHistory(OrderStatus.PLACED, "Order placed");

        carts.deleteAll(cart);
        return orders.save(order);
    }

    @Transactional
    public Order cancel(User user, Long orderId) {
        Order order = orders.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> new RuntimeException("Order Not Found"));
        if (order.getStatus() != OrderStatus.PLACED && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new RuntimeException("Order can no longer be cancelled");
        }
        restoreStock(order);
        order.addStatusHistory(OrderStatus.CANCELLED, "Cancelled by customer");
        return order;
    }

    // ---------------- admin ----------------

    @Transactional
    public Order assignPartner(Long orderId, Long partnerId) {
        Order order = find(orderId);
        ensureOpen(order);
        DeliveryPartner partner = partners.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Delivery partner not found"));
        if (!partner.getIsActive()) {
            throw new RuntimeException("Delivery partner is inactive");
        }
        order.setDeliveryPartner(partner);
        order.addStatusHistory(order.getStatus(), "Assigned to " + partner.getName());
        return order;
    }

    @Transactional
    public Order updateStatus(Long orderId, OrderStatus status, String note) {
        Order order = find(orderId);
        ensureOpen(order);
        if (status == OrderStatus.CANCELLED) {
            restoreStock(order);
        }
        order.addStatusHistory(status, note);
        return order;
    }

    // ---------------- delivery partner ----------------

    @Transactional
    public Order partnerUpdateStatus(Long partnerId, Long orderId, OrderStatus status) {
        if (!PARTNER_STATUSES.contains(status)) {
            throw new RuntimeException("Partners can only set PICKED_UP or OUT_FOR_DELIVERY");
        }
        Order order = findForPartner(partnerId, orderId);
        ensureOpen(order);
        order.addStatusHistory(status, "Updated by delivery partner");
        return order;
    }

    @Transactional
    public Order updateLocation(Long partnerId, Long orderId, double lat, double lng) {
        Order order = findForPartner(partnerId, orderId);
        ensureOpen(order);
        LiveLocation location = new LiveLocation();
        location.setLat(lat);
        location.setLng(lng);
        order.setLiveLocation(location);
        return order;
    }

    @Transactional
    public Order deliver(Long partnerId, Long orderId, String otp) {
        Order order = findForPartner(partnerId, orderId);
        ensureOpen(order);
        if (!order.getDeliveryOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }
        if (order.getPaymentMethod() == PaymentMethod.COD) {
            order.setPaymentStatus(PaymentStatus.PAID);
        }
        order.addStatusHistory(OrderStatus.DELIVERED, "Delivered with OTP");
        return order;
    }

    // ---------------- helpers ----------------

    private Order find(Long id) {
        return orders.findById(id).orElseThrow(() -> new RuntimeException("Order Not Found"));
    }

    private Order findForPartner(Long partnerId, Long orderId) {
        return orders.findByIdAndDeliveryPartnerId(orderId, partnerId)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));
    }

    private void ensureOpen(Order order) {
        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order is already " + order.getStatus());
        }
    }

    private void restoreStock(Order order) {
        for (OrderItem item : order.getItems()) {
            Product p = item.getProduct();
            if (p != null) {
                p.setStock(p.getStock() + item.getQuantity());
            }
        }
    }

    private ShippingAddress snapshotOf(Address a, User user) {
        ShippingAddress s = new ShippingAddress();
        s.setName(user.getUsername());
        s.setPhone(user.getPhone());
        s.setAddress(a.getAddressLine());
        s.setCity(a.getCity());
        s.setState(a.getState());
        s.setZip(a.getZip());
        s.setLat(a.getLat());
        s.setLng(a.getLng());
        return s;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }


}
