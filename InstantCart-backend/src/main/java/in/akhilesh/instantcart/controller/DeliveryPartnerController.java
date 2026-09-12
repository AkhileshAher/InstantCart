package in.akhilesh.instantcart.controller;

import in.akhilesh.instantcart.dto.delivery.DeliveryPartnerResponse;
import in.akhilesh.instantcart.entity.DeliveryPartner;
import in.akhilesh.instantcart.entity.Order;
import in.akhilesh.instantcart.entity.enums.OrderStatus;
import in.akhilesh.instantcart.security.JwtPrincipal;
import in.akhilesh.instantcart.service.DeliveryPartnerService;
import in.akhilesh.instantcart.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery-partners")
@RequiredArgsConstructor
public class DeliveryPartnerController {

    private final DeliveryPartnerService deliveryPartnerService;
    private final OrderService orderService;

    @GetMapping("/{partnerId}")
    public ResponseEntity<DeliveryPartnerResponse> getDeliveryPartner(@PathVariable ObjectId partnerId) {
        DeliveryPartnerResponse response = deliveryPartnerService.getDeliveryPartner(partnerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeliveryPartnerResponse> createDeliveryPartner(@RequestBody DeliveryPartner deliveryPartner) {

        DeliveryPartnerResponse response = deliveryPartnerService.createDeliveryPartner(deliveryPartner);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // SET PARTNER STATUS
    @PatchMapping("/{partnerId}/status")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeliveryPartnerResponse> updateStatus(
            @PathVariable ObjectId partnerId,
            @RequestParam Boolean active
    ) {
        DeliveryPartnerResponse deliveryPartner = deliveryPartnerService.updateStatus(partnerId, active);
        return ResponseEntity.ok(deliveryPartner);
    }

    @PutMapping("/{orderId}/deliver")
//    @PreAuthorize("hasRole('DELIVERY_PARTNER')")
    public ResponseEntity<Order> markAsDelivered(
            @PathVariable ObjectId orderId,
            @RequestParam String otp,
            Authentication authentication
    ) {

//        JwtPrincipal partner = (JwtPrincipal) authentication.getPrincipal();
//        ObjectId deliveryPartnerId = partner.getUserId();
        ObjectId deliveryPartnerId = new ObjectId("6aa576f51658fbe8ead0819e");
        Order order = orderService.markOrderAsDelivered(
                orderId,
                otp,
                deliveryPartnerId
        );
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/assign/{orderId}")
    public ResponseEntity<Order> assignOrder(@PathVariable ObjectId orderId,@RequestParam ObjectId partnerId) {
        Order order = orderService.assignDeliveryPartner(orderId, partnerId);
        return ResponseEntity.ok(order);
    }

    // DELIVERY PARTNER ACCESS TO CHANGE STATUS TO DELIVERED CANCELLED AND OUT FOR DELIVERY
    @PatchMapping("/status-update/{orderId}")
    public ResponseEntity<Order> changeOrderStatus(@PathVariable ObjectId orderId, @RequestParam OrderStatus status) {
        return orderService.changeStatus(orderId,status);
    }

}