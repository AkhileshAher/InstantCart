package in.akhilesh.instantcart.repository;

import in.akhilesh.instantcart.entity.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, ObjectId> {
    List<Product> findByStockGreaterThan(Integer stock);

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByVendorId(ObjectId vendorId);

    Optional<Product> findByIdAndVendorId(
            ObjectId productId,
            ObjectId vendorId
    );

    List<Product> findByCategoryIgnoreCaseAndStockGreaterThan(
            String category,
            Integer stock
    );
}