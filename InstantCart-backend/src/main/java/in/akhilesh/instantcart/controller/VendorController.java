package in.akhilesh.instantcart.controller;

import in.akhilesh.instantcart.dto.admin.AdminOrderResponse;
import in.akhilesh.instantcart.dto.admin.DashboardResponse;
import in.akhilesh.instantcart.entity.DeliveryPartner;
import in.akhilesh.instantcart.service.DashboardService;
import in.akhilesh.instantcart.service.DeliveryPartnerService;
import in.akhilesh.instantcart.service.OrderService;
import in.akhilesh.instantcart.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class VendorController {

    private final DashboardService dashboardService;
    private final OrderService orderService;
    private final DeliveryPartnerService deliveryPartnerService;
    private final ProductService productService;

    @GetMapping("/dashboard")
//    @PreAuthorize("hasRole('ADMIN')")
    public DashboardResponse getDashboard() {
        return dashboardService.getDashboard();
    }

    @GetMapping("/orders")
//    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminOrderResponse> getAllOrders() {
        return orderService.getAllOrders()
                .stream()
//                .map(/* map to AdminOrderResponse */)
                .toList();
    }

    @GetMapping("/delivery-partners")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DeliveryPartner>> getDeliveryPartners() {

        List<DeliveryPartner> allDeliveryPartners = deliveryPartnerService.getAllDeliveryPartners();

        return ResponseEntity.ok(allDeliveryPartners);
    }
}