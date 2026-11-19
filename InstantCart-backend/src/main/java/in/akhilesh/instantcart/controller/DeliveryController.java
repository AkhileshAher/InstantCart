package in.akhilesh.instantcart.controller;

import in.akhilesh.instantcart.dto.LocationRequest;
import in.akhilesh.instantcart.dto.OrderResponse;
import in.akhilesh.instantcart.dto.OtpRequest;
import in.akhilesh.instantcart.dto.StatusRequest;
import in.akhilesh.instantcart.payload.ApiResponse;
import in.akhilesh.instantcart.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Endpoints used by the delivery partner app.
 * partnerId comes from the path for now; once partner login exists,
 * read it from the authenticated principal instead.
 */
@RestController
@RequestMapping("/api/delivery/{partnerId}/orders")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> myOrders(@PathVariable Long partnerId) {
        List<OrderResponse> orders = deliveryService.getMyOrders(partnerId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Orders Fetched Successfully",
                        orders
                ));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(@PathVariable Long partnerId, @PathVariable Long orderId,
                                      @Valid @RequestBody StatusRequest req) {
        OrderResponse updatedOrderStatus = deliveryService.updateOrderStatus(partnerId, orderId, req);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Orders Fetched Successfully",
                        updatedOrderStatus
                ));
    }

    @PutMapping("/{orderId}/location")
    public ResponseEntity<ApiResponse<OrderResponse>> updateLocation(@PathVariable Long partnerId, @PathVariable Long orderId,
                                        @Valid @RequestBody LocationRequest req) {
        OrderResponse updatedOrderLocation = deliveryService.updateOrderLocation(partnerId, orderId, req);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Orders Fetched Successfully",
                        updatedOrderLocation
                ));
    }

    @PostMapping("/{orderId}/deliver")
    public ResponseEntity<ApiResponse<OrderResponse>> deliver(@PathVariable Long partnerId, @PathVariable Long orderId,
                                 @Valid @RequestBody OtpRequest req) {
        OrderResponse deliveredOrder = deliveryService.deliverOrder(partnerId, orderId, req);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Orders Fetched Successfully",
                        deliveredOrder
                ));
    }
}
