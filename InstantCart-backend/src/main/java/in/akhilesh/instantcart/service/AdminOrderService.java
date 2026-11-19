package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.OrderResponse;
import in.akhilesh.instantcart.dto.StatusRequest;
import in.akhilesh.instantcart.entity.Order;
import in.akhilesh.instantcart.entity.OrderStatus;
import in.akhilesh.instantcart.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final OrderService orderService;
    private final OrderRepository orders;


    public List<OrderResponse> getAllOrders(OrderStatus status) {
        List<Order> result = (status == null)
                ? orders.findAllByOrderByCreatedAtDesc()
                : orders.findByStatusOrderByCreatedAtDesc(status);
        return result.stream().map(o -> OrderResponse.from(o, false)).toList();
    }

    public OrderResponse getOrderById(Long id) {
        return orders.findById(id)
                .map(o -> OrderResponse.from(o, false))
                .orElseThrow(() ->new RuntimeException("Order Not Found"));
    }

    public OrderResponse assignPartnerToOrder(Long id, Long partnerId) {
        return OrderResponse.from(orderService.assignPartner(id, partnerId), false);
    }

    public OrderResponse updateOrderStatus(Long id, @Valid StatusRequest req) {
        return OrderResponse.from(orderService.updateStatus(id, req.status(), req.note()), false);
    }
}
