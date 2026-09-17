package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.admin.DashboardResponse;
import in.akhilesh.instantcart.dto.delivery.DeliveryPartnerResponse;
import in.akhilesh.instantcart.dto.order.OrderResponse;
import in.akhilesh.instantcart.dto.user.UserResponse;
import in.akhilesh.instantcart.entity.DeliveryPartner;
import in.akhilesh.instantcart.entity.Order;
import in.akhilesh.instantcart.entity.User;
import in.akhilesh.instantcart.entity.enums.UserRole;
import in.akhilesh.instantcart.repository.DeliveryPartnerRepository;
import in.akhilesh.instantcart.repository.OrderRepository;
import in.akhilesh.instantcart.repository.ProductRepository;
import in.akhilesh.instantcart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final DeliveryPartnerRepository deliveryPartnerRepository;

    @PreAuthorize(value = "hasRole('VENDOR')")
    public DashboardResponse getDashboard() {

        long totalUsers = userRepository.countByRole(UserRole.CUSTOMER);
        long totalProducts = productRepository.count();
        long totalOrders = orderRepository.count();
        long totalPartners = deliveryPartnerRepository.count();
        long outOfStock = productRepository.countByStock(0);

        DashboardResponse response = new DashboardResponse();
        response.setTotalUsers(totalUsers);
        response.setTotalProducts(totalProducts);
        response.setTotalOrders(totalOrders);
        response.setTotalPartners(totalPartners);
        response.setOutOfStock(outOfStock);


        response.setRecentOrders(
                orderRepository.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")))
                        .getContent()
                        .stream()
                        .map(this::mapOrderToResponse)
                        .toList()
        );

        return response;
    }

    private OrderResponse mapOrderToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId().toHexString());
        response.setUserId(order.getUserId().toHexString());
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

        if(order.getDeliveryPartnerId() != null) {
            response.setDeliveryPartnerId(order.getDeliveryPartnerId().toHexString());
        }
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
        if(order.getDeliveryOtp() != null) {
            response.setDeliveryOtp(order.getDeliveryOtp());
        }

        response.setLiveLocation(order.getLiveLocation());
        response.setIsPaid(order.getIsPaid());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        return response;
    }

}