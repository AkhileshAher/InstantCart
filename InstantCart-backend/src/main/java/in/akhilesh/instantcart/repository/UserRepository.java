package in.akhilesh.instantcart.repository;

import in.akhilesh.instantcart.entity.User;
import in.akhilesh.instantcart.entity.enums.UserRole;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    long countByRole(UserRole userRole);
}