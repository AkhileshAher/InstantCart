package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.LocationRequest;
import in.akhilesh.instantcart.dto.OrderResponse;
import in.akhilesh.instantcart.dto.OtpRequest;
import in.akhilesh.instantcart.dto.StatusRequest;
import in.akhilesh.instantcart.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final OrderService orderService;
    private final OrderRepository orders;


    public List<OrderResponse> getMyOrders(Long partnerId) {
         return orders.findByDeliveryPartnerIdOrderByCreatedAtDesc(partnerId)
                .stream()
                 .map(o -> OrderResponse.from(o, false))
                 .toList();
    }

    public OrderResponse updateOrderStatus(Long partnerId, Long orderId, @Valid StatusRequest req) {
        return OrderResponse.from(orderService.partnerUpdateStatus(partnerId, orderId, req.status()), false);
    }

    public OrderResponse updateOrderLocation(Long partnerId, Long orderId, @Valid LocationRequest req) {
        return OrderResponse.from(orderService.updateLocation(partnerId, orderId, req.lat(), req.lng()), false);

    }

    public OrderResponse deliverOrder(Long partnerId, Long orderId, @Valid OtpRequest req) {
        return OrderResponse.from(orderService.deliver(partnerId, orderId, req.otp()), false);
    }
}
