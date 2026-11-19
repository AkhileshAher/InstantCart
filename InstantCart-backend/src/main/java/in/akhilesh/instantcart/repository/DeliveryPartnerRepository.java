package in.akhilesh.instantcart.repository;

import in.akhilesh.instantcart.entity.DeliveryPartner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryPartnerRepository extends JpaRepository<DeliveryPartner, Long> {
    boolean existsByEmail(String email);
}
