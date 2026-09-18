package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.delivery.DeliveryPartnerResponse;
import in.akhilesh.instantcart.entity.DeliveryPartner;
import in.akhilesh.instantcart.repository.DeliveryPartnerRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryPartnerService {

    private final DeliveryPartnerRepository repository;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize(value = "hasRole('VENDOR')")
    @Cacheable(value = "allDeliveryPartners", key = "'all'")
    public List<DeliveryPartnerResponse> getAllDeliveryPartners() {
        return repository.findAll().stream()
                .map(this::mapToDeliveryResponse)
                .toList();
    }

    @Cacheable(value = "activeDeliveryPartners", key = "'active'")
    public List<DeliveryPartner> getActiveDeliveryPartners() {
        return repository.findByIsActiveTrue();
    }

    @PreAuthorize(value = "hasRole('VENDOR')")
    @Cacheable(value = "deliveryPartner", key = "#id.toHexString()")
    public DeliveryPartnerResponse getDeliveryPartner(ObjectId id) {
        DeliveryPartner deliveryPartner = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery partner not found"));
        return mapToDeliveryResponse(deliveryPartner);
    }

    @Transactional
    @CacheEvict(value = "allDeliveryPartners", key = "'all'")
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
    @Caching(evict = {
            @CacheEvict(value = "allDeliveryPartners", key = "'all'"),
            @CacheEvict(value = "activeDeliveryPartners", key = "'active'"),
            @CacheEvict(value = "deliveryPartner", key = "#partnerId.toHexString()")
    })
    @PreAuthorize(value = "hasRole('VENDOR')")
    public DeliveryPartnerResponse updateStatus(ObjectId partnerId, Boolean status) {
        DeliveryPartner partner = repository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Delivery Partner with this Id does not exist " + partnerId));

        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        if (status != null && status.equals(partner.getIsActive())) {
            throw new RuntimeException("status Already available");
        }

        partner.setIsActive(status);
        DeliveryPartner saved = repository.save(partner);
        return mapToDeliveryResponse(saved);
    }
}