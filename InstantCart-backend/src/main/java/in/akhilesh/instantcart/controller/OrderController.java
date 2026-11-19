package in.akhilesh.instantcart.controller;

import in.akhilesh.instantcart.dto.OrderResponse;
import in.akhilesh.instantcart.dto.PlaceOrderRequest;
import in.akhilesh.instantcart.entity.Order;
import in.akhilesh.instantcart.entity.User;
import in.akhilesh.instantcart.payload.ApiResponse;
import in.akhilesh.instantcart.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/** Customer orders. The customer is the only one who sees the delivery OTP. */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> place(@AuthenticationPrincipal User user, @Valid @RequestBody PlaceOrderRequest req) {
        Order order = orderService.place(user, req);
        OrderResponse orderResponse = OrderResponse.from(order, true);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.CREATED.value(),
                        "Order placed successfully",
                        orderResponse
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> myOrders(@AuthenticationPrincipal User user) {
        List<OrderResponse> allOrder = orderService.getAllOrder(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "All Order fetched successfully",
                        allOrder
                ));
    }

    /** Also used for live tracking: the response contains status history and liveLocation. */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@AuthenticationPrincipal User user, @PathVariable Long id) {
        OrderResponse userOrder = orderService.getUserOrder(user, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Order fetched successfully",
                        userOrder
                ));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancel(@AuthenticationPrincipal User user, @PathVariable Long id) {
        Order cancelled = orderService.cancel(user, id);
        OrderResponse orderResponse = OrderResponse.from(cancelled, true);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Order cancelled successfully",
                        orderResponse
                ));
    }
}
