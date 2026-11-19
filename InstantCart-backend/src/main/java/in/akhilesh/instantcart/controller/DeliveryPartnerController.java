package in.akhilesh.instantcart.controller;

import in.akhilesh.instantcart.dto.DeliveryPartnerRequest;
import in.akhilesh.instantcart.dto.DeliveryPartnerResponse;
import in.akhilesh.instantcart.payload.ApiResponse;
import in.akhilesh.instantcart.service.DeliveryPartnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

// Used by admin to manage delivery partner

@RestController
@RequestMapping("/api/admin/delivery-partners")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class DeliveryPartnerController {

    private final DeliveryPartnerService partnerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeliveryPartnerResponse>>> getAll() {
        List<DeliveryPartnerResponse> allPartners = partnerService.getAllPartners();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "All Delivery Partners fetched Successfully",
                        allPartners
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DeliveryPartnerResponse>> getById(@PathVariable Long id) {
        DeliveryPartnerResponse partner = partnerService.getPartnerById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Delivery Partner fetched Successfully",
                        partner
                ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeliveryPartnerResponse>> create(@Valid @RequestBody DeliveryPartnerRequest req) {
        DeliveryPartnerResponse deliveryPartner = partnerService.createDeliveryPartner(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.CREATED.value(),
                        "Delivery Partner created Successfully",
                        deliveryPartner
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DeliveryPartnerResponse>> update(@PathVariable Long id, @Valid @RequestBody DeliveryPartnerRequest req) {
        DeliveryPartnerResponse updatePartner = partnerService.updatePartner(id, req);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.NO_CONTENT.value(),
                        "Delivery Partner updated Successfully",
                        updatePartner
                ));
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<ApiResponse<DeliveryPartnerResponse>> toggleActive(@PathVariable Long id) {
        DeliveryPartnerResponse partnerResponse = partnerService.toggleActive(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.NO_CONTENT.value(),
                        "Delivery Partner status toggled Successfully",
                        partnerResponse
                ));
    }


}
