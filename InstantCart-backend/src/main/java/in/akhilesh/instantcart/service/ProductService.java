package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.ProductRequest;
import in.akhilesh.instantcart.dto.ProductResponse;
import in.akhilesh.instantcart.entity.Product;
import in.akhilesh.instantcart.repository.CategoryRepository;
import in.akhilesh.instantcart.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;


    public List<ProductResponse> searchProducts(Long categoryId, String q) {
        List<Product> result = (categoryId == null)
                ? productRepo.findByIsActiveTrueAndNameContainingIgnoreCase(q)
                : productRepo.findByCategoryIdAndIsActiveTrueAndNameContainingIgnoreCase(categoryId, q);
        return result.stream().map(ProductResponse::from).toList();
    }

    public ProductResponse getProduct(Long id) {
        Product product = productRepo.findById(id)
                .filter(Product::getIsActive)
                .orElseThrow(() -> new RuntimeException("Product not Found"));
        return ProductResponse.from(product);
    }

    public List<ProductResponse> getAllAdminProducts() {
        return productRepo.findAll().stream().map(ProductResponse::from).toList();
    }

    public ProductResponse createProduct(@Valid ProductRequest req) {
        Product product = new Product();
        apply(product, req);
        return ProductResponse.from(productRepo.save(product));
    }

    public ProductResponse updateProduct(Long id, @Valid ProductRequest req) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));
        apply(product, req);
        return ProductResponse.from(productRepo.save(product));
    }

    public void deleteProduct(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));
        product.setIsActive(false);
        productRepo.save(product);
    }

    private void apply(Product p, ProductRequest r) {
        p.setCategory(categoryRepo.findById(r.categoryId())
                .orElseThrow(() -> new RuntimeException("Category Not Found")));
        p.setName(r.name());
        p.setDescription(r.description() == null ? "" : r.description());
        p.setPrice(r.price());
        p.setOriginalPrice(r.originalPrice() == null ? r.price() : r.originalPrice());
        p.setDiscountPercent(r.discountPercent() == null ? 0 : r.discountPercent());
        p.setImage(r.image());
        p.setUnit(r.unit() == null ? "piece" : r.unit());
        p.setStock(r.stock() == null ? 0 : r.stock());
        p.setIsOrganic(Boolean.TRUE.equals(r.isOrganic()));
    }
}