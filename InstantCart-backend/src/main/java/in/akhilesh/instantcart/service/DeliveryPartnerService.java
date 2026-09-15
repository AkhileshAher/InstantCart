package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.delivery.DeliveryPartnerResponse;
import in.akhilesh.instantcart.entity.DeliveryPartner;
import in.akhilesh.instantcart.repository.DeliveryPartnerRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryPartnerService {

    private final DeliveryPartnerRepository repository;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize(value = "hasRole('VENDOR')")
    public List<DeliveryPartnerResponse> getAllDeliveryPartners() {
        return repository.findAll().stream()
                .map(this::mapToDeliveryResponse)
                .toList();
    }

    public List<DeliveryPartner> getActiveDeliveryPartners() {
        return repository.findByIsActiveTrue();
    }

    @PreAuthorize(value = "hasRole('VENDOR')")
    public DeliveryPartnerResponse getDeliveryPartner(ObjectId id) {
        DeliveryPartner deliveryPartner = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery partner not found"));
        return mapToDeliveryResponse(deliveryPartner);
    }

    @Transactional
    @PreAuthorize(value = "hasRole('VENDOR')")
    public DeliveryPartnerResponse createDeliveryPartner(DeliveryPartner partner) {
        if (repository.existsByEmail(partner.getEmail())) {
            throw new RuntimeException("Delivery partner already exists");
        }
        partner.setPassword(passwordEncoder.encode(partner.getPassword()));
        DeliveryPartner saved = repository.save(partner);
        return mapToDeliveryResponse(saved);
    }

    private DeliveryPartnerResponse mapToDeliveryResponse(DeliveryPartner saved) {
        DeliveryPartnerResponse partner = new DeliveryPartnerResponse();
        partner.setId(saved.getId().toHexString());
        partner.setName(saved.getName());
        partner.setEmail(saved.getEmail());
        partner.setPhone(saved.getPhone());
        partner.setAvatar(saved.getAvatar());
        partner.setIsActive(saved.getIsActive());
        partner.setVehicleType(saved.getVehicleType());
        partner.setCreatedAt(saved.getCreatedAt());
        partner.setUpdatedAt(saved.getUpdatedAt());
        return partner;
    }

    // Active or Deactivate Partner Or Not
    @Transactional
    @PreAuthorize(value = "hasRole('VENDOR')")
    public DeliveryPartnerResponse updateStatus(ObjectId partnerId, Boolean status) {
        DeliveryPartner partner = repository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Delivery Partner with this Id does not exist " + partnerId));
        if(status == partner.getIsActive()) {
            throw new RuntimeException("status Already available");
        }
        partner.setIsActive(status);
        DeliveryPartner saved = repository.save(partner);
        return mapToDeliveryResponse(saved);
    }
}