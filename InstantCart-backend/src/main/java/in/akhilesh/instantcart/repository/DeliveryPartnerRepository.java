package in.akhilesh.instantcart.repository;

import in.akhilesh.instantcart.entity.DeliveryPartner;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryPartnerRepository extends MongoRepository<DeliveryPartner, ObjectId> {

    Optional<DeliveryPartner> findByEmail(String email);

    boolean existsByEmail(String email);

    List<DeliveryPartner> findByIsActiveTrue();

    List<DeliveryPartner> findByIsActive(Boolean isActive);
}