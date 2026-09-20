package in.akhilesh.instantcart.controller;

import in.akhilesh.instantcart.dto.order.CreateOrderRequest;
import in.akhilesh.instantcart.dto.order.OrderResponse;
import in.akhilesh.instantcart.entity.LiveLocation;
import in.akhilesh.instantcart.entity.Order;
import in.akhilesh.instantcart.entity.enums.OrderStatus;
import in.akhilesh.instantcart.security.JwtPrincipal;
import in.akhilesh.instantcart.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable ObjectId orderId,
            Authentication authentication
    ) {

        JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();
        ObjectId userId = principal.getUserId();

        OrderResponse response = orderService.getOrder(orderId, userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrders(
            Authentication authentication,
            @RequestParam(value = "status", required = false) OrderStatus status
    ) {

        JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();
        ObjectId userId = principal.getUserId();
        List<OrderResponse> userOrders = orderService.getUserOrders(userId, status);
        return ResponseEntity.ok(userOrders);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            Authentication authentication,
            @RequestBody @Valid CreateOrderRequest request
    ) {

        JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();
        ObjectId userId = principal.getUserId();
        OrderResponse response = orderService.createOrder(userId, request,false);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orderId}/location")
    public ResponseEntity<LiveLocation> getOrderLocation(
            @PathVariable ObjectId orderId,
            Authentication authentication
    ) {

        JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();
        ObjectId userId = principal.getUserId();

        return ResponseEntity.ok(
                orderService.getOrderLocation(
                        orderId,
                        userId
                )
        );
    }


    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrdersToDeliver(
            Authentication authentication,
            @RequestParam String status
    ) {
        JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();

        ObjectId deliveryPartnerId = principal.getUserId();

        List<OrderResponse> ordersList =
                orderService.getOrdersOfDeliveryPartner(
                        deliveryPartnerId,
                        status
                );

        return ResponseEntity.ok(ordersList);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable ObjectId orderId, Authentication authentication) {

        JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();
        ObjectId userId = principal.getUserId();

        orderService.deleteOrder(userId,orderId);

        return ResponseEntity.noContent().build();

    }

}