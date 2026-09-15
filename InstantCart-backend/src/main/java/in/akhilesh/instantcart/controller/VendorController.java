package in.akhilesh.instantcart.controller;

import in.akhilesh.instantcart.dto.admin.AdminOrderResponse;
import in.akhilesh.instantcart.dto.admin.DashboardResponse;
import in.akhilesh.instantcart.dto.delivery.DeliveryPartnerResponse;
import in.akhilesh.instantcart.dto.order.OrderResponse;
import in.akhilesh.instantcart.entity.DeliveryPartner;
import in.akhilesh.instantcart.entity.enums.OrderStatus;
import in.akhilesh.instantcart.service.DashboardService;
import in.akhilesh.instantcart.service.DeliveryPartnerService;
import in.akhilesh.instantcart.service.OrderService;
import in.akhilesh.instantcart.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor")
@RequiredArgsConstructor
public class VendorController {

    private final DashboardService dashboardService;
    private final OrderService orderService;
    private final DeliveryPartnerService deliveryPartnerService;
    private final ProductService productService;

    @GetMapping("/dashboard")
    public DashboardResponse getDashboard() {
        return dashboardService.getDashboard();
    }

    @GetMapping("/orders")
    public List<AdminOrderResponse> getAllOrders() {
        return orderService.getAllOrders()
                .stream()
//                .map(/* map to AdminOrderResponse */)
                .toList();
    }

    @GetMapping("/delivery-partners")
    public ResponseEntity<List<DeliveryPartnerResponse>> getDeliveryPartners() {

        List<DeliveryPartnerResponse> allDeliveryPartners = deliveryPartnerService.getAllDeliveryPartners();

        return ResponseEntity.ok(allDeliveryPartners);
    }

    @PatchMapping("/update/{orderId}")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable ObjectId orderId, @RequestParam OrderStatus status) {
        OrderResponse response = orderService.changeOrderStatus(orderId, status);
        return ResponseEntity.ok(response);
    }

}