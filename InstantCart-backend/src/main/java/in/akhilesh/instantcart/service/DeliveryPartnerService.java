package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.delivery.DeliveryPartnerResponse;
import in.akhilesh.instantcart.entity.DeliveryPartner;
import in.akhilesh.instantcart.repository.DeliveryPartnerRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryPartnerService {

    private final DeliveryPartnerRepository repository;
    private final PasswordEncoder passwordEncoder;

    public List<DeliveryPartner> getAllDeliveryPartners() {
        return repository.findAll();
    }

    public List<DeliveryPartner> getActiveDeliveryPartners() {
        return repository.findByIsActiveTrue();
    }

    public DeliveryPartnerResponse getDeliveryPartner(ObjectId id) {
        DeliveryPartner deliveryPartner = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery partner not found"));
        return mapToDeliveryResponse(deliveryPartner);
    }

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
    public DeliveryPartnerResponse updateStatus(ObjectId partnerId, Boolean status) {
        DeliveryPartner partner = repository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Delivery Partner with this Id does not exist " + partnerId));
        partner.setIsActive(status);
        DeliveryPartner saved = repository.save(partner);
        return mapToDeliveryResponse(saved);
    }
}