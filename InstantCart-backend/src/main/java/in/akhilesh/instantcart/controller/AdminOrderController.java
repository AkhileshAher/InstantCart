package in.akhilesh.instantcart.controller;

import in.akhilesh.instantcart.dto.OrderResponse;
import in.akhilesh.instantcart.dto.StatusRequest;
import in.akhilesh.instantcart.entity.OrderStatus;
import in.akhilesh.instantcart.payload.ApiResponse;
import in.akhilesh.instantcart.service.AdminOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAll(@RequestParam(required = false) OrderStatus status) {
        List<OrderResponse> allOrders = adminOrderService.getAllOrders(status);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Admin all Orders fetched successfully",
                        allOrders
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOne(@PathVariable Long id) {
        OrderResponse order = adminOrderService.getOrderById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Order fetched successfully",
                        order
                ));
    }

    @PutMapping("/{id}/assign/{partnerId}")
    public ResponseEntity<ApiResponse<OrderResponse>> assignPartner(@PathVariable Long id, @PathVariable Long partnerId) {
        OrderResponse orderResponse = adminOrderService.assignPartnerToOrder(id, partnerId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Admin all Orders fetched successfully",
                        orderResponse
                ));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest req) {
        OrderResponse updatedOrderStatus = adminOrderService.updateOrderStatus(id, req);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Admin all Orders fetched successfully",
                        updatedOrderStatus
                ));
    }
}
