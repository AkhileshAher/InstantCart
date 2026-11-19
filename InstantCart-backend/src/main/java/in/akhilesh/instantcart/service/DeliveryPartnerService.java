package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.DeliveryPartnerRequest;
import in.akhilesh.instantcart.dto.DeliveryPartnerResponse;
import in.akhilesh.instantcart.entity.DeliveryPartner;
import in.akhilesh.instantcart.repository.DeliveryPartnerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryPartnerService {

    private final DeliveryPartnerRepository partnerRepository;
    private final PasswordEncoder passwordEncoder;

    public List<DeliveryPartnerResponse> getAllPartners() {
         return partnerRepository.findAll()
                .stream()
                .map(DeliveryPartnerResponse::from)
                 .toList();
    }

    public DeliveryPartnerResponse getPartnerById(Long id) {
        return DeliveryPartnerResponse.from(find(id));
    }

    public DeliveryPartnerResponse createDeliveryPartner(@Valid DeliveryPartnerRequest req) {
        if (req.password() == null || req.password().isBlank()) {
            throw new RuntimeException("Password is required");
        }
        if (partnerRepository.existsByEmail(req.email())) {
            throw new RuntimeException("Email already registered");
        }
        DeliveryPartner partner = new DeliveryPartner();
        apply(partner, req);
        partner.setPassword(passwordEncoder.encode(req.password()));
        return DeliveryPartnerResponse.from(partnerRepository.save(partner));
    }


    public DeliveryPartnerResponse updatePartner(Long id, @Valid DeliveryPartnerRequest req) {
        DeliveryPartner partner = find(id);
        apply(partner, req);
        if (req.password() != null && !req.password().isBlank()) {
            partner.setPassword(passwordEncoder.encode(req.password()));
        }
        return DeliveryPartnerResponse.from(partnerRepository.save(partner));
    }


    public DeliveryPartnerResponse toggleActive(Long id) {
        DeliveryPartner partner = find(id);
        partner.setIsActive(!partner.getIsActive());
        return DeliveryPartnerResponse.from(partnerRepository.save(partner));
    }


    private DeliveryPartner find(Long id) {
        return partnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery Partner Not Found"));
    }

    private void apply(DeliveryPartner p, DeliveryPartnerRequest r) {
        p.setName(r.name());
        p.setEmail(r.email());
        p.setPhone(r.phone());
    }
}