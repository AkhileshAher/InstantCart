package in.akhilesh.instantcart.repository;

import in.akhilesh.instantcart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByCategoryId(Long categoryId);
    List<Product> findByIsActiveTrueAndNameContainingIgnoreCase(String name);
    List<Product> findByCategoryIdAndIsActiveTrueAndNameContainingIgnoreCase(Long categoryId, String name);
}
