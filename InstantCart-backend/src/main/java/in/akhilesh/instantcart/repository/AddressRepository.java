package in.akhilesh.instantcart.repository;

import in.akhilesh.instantcart.entity.Address;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends MongoRepository<Address, ObjectId> {
    List<Address> findByUserId(ObjectId userId);

    Optional<Address> findByIdAndUserId(ObjectId id, ObjectId userId);

    void deleteByIdAndUserId(ObjectId id, ObjectId userId);
}