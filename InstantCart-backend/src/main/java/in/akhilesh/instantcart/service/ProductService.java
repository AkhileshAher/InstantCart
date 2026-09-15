package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.product.ProductRequest;
import in.akhilesh.instantcart.dto.product.ProductResponse;
import in.akhilesh.instantcart.entity.Product;
import in.akhilesh.instantcart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CloudinaryImageService cloudinary;


    public List<ProductResponse> popularProducts(Integer limit) {

        Page<Product> productPage =
                productRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "rating")));

        List<ProductResponse> products = productPage.getContent()
                .stream()
                .map(this::mapProductToResponse)
                .toList();

        return products;
    }


    public List<ProductResponse> searchProducts(String query) {

        if (query == null || query.isBlank()) {
            return List.of();
        }

        return productRepository.searchProducts(query.trim())
                .stream()
                .map(this::mapProductToResponse)
                .toList();
    }


    public List<ProductResponse> getProducts(
            String sort,
            String category,
            Double minPrice,
            Double maxPrice
    ) {

        List<Product> products;
        if (category != null && !category.isBlank()) {
            products = productRepository.findByCategoryIgnoreCase(category);
        } else {
            products = productRepository.findAll();
        }

        // Price filtering
        if (minPrice != null) {
            products = products.stream()
                    .filter(p -> p.getPrice() >= minPrice)
                    .toList();
        }

        if (maxPrice != null) {
            products = products.stream()
                    .filter(p -> p.getPrice() <= maxPrice)
                    .toList();
        }

        // Sorting
        if ("price-asc".equalsIgnoreCase(sort)) {
            products = products.stream()
                    .sorted(Comparator.comparing(Product::getPrice))
                    .toList();
        }
        else if ("price-desc".equalsIgnoreCase(sort)) {
            products = products.stream()
                    .sorted(Comparator.comparing(
                            Product::getPrice
                    ).reversed())
                    .toList();
        }
        else if ("rating".equalsIgnoreCase(sort)) {

            products = products.stream()
                    .sorted(Comparator.comparing(
                            Product::getRating
                    ).reversed())
                    .toList();
        }
        return products.stream().map(this::mapProductToResponse).toList();
    }

    public List<ProductResponse> getFlashDeals() {

        return productRepository
                .findByStockGreaterThan(0)
                .stream()
                .map(this::calculateDiscount)
                .filter(p -> p.getDiscount() > 0)
                .sorted(
                        Comparator.comparing(
                                Product::getDiscount
                        ).reversed()
                )
                .map(this::mapProductToResponse)
                .toList();
    }

    public ProductResponse getProduct(ObjectId productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        ProductResponse response = mapProductToResponse(product);
        return response;
    }

    @Transactional
    @PreAuthorize("hasRole('VENDOR') and #vendorId == authentication.principal.userId")
    public ProductResponse addProduct(ObjectId vendorId, ProductRequest request) {

        Product product = mapRequestToProduct(request,vendorId);
        Product savedProduct = productRepository.save(product);
        ProductResponse response = mapProductToResponse(savedProduct);
        return response;
    }

    @Transactional
    @PreAuthorize("hasRole('VENDOR') and #vendorId == authentication.principal.userId")
    public ProductResponse updateProduct(ObjectId productId, ObjectId vendorId, ProductRequest request) {

        Product product = productRepository.findByIdAndVendorId(productId, vendorId)
                .orElseThrow(() ->
                        new AccessDeniedException("You are not allowed to update this product")
                );

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setImage(request.getImage());
        product.setCategory(request.getCategory());
        product.setUnit(request.getUnit());
        product.setStock(request.getStock());
        product.setIsOrganic(request.getIsOrganic());

        Product updatedProduct = productRepository.save(product);
        return mapProductToResponse(updatedProduct);
    }

    @Transactional
    @PreAuthorize("hasRole('VENDOR') and #vendorId == authentication.principal.userId")
    public void deleteProduct(ObjectId productId, ObjectId vendorId) {

        Product product = productRepository
                .findByIdAndVendorId(productId, vendorId)
                .orElseThrow(() -> new AccessDeniedException("You are not allowed to delete this product"));
        if(product.getStock() == 0) {
            throw new RuntimeException("Product is Already Out of Stock");
        }
        product.setStock(0);
        productRepository.save(product);
    }

    private Product calculateDiscount(Product product) {

        if (product.getOriginalPrice() != null &&
                product.getOriginalPrice() > 0 &&
                product.getPrice() != null) {

            double discount =
                    ((product.getOriginalPrice() - product.getPrice()) / product.getOriginalPrice()) * 100;

            product.setDiscount((int) Math.round(discount));
        } else {
            product.setDiscount(0);
        }

        return product;
    }



    private ProductResponse mapProductToResponse(Product savedProduct) {
        ProductResponse response = new ProductResponse();
        response.setId(savedProduct.getId().toHexString());
        response.setName(savedProduct.getName());
        response.setImage(savedProduct.getImage());
        response.setDescription(savedProduct.getDescription());
        response.setCategory(savedProduct.getCategory());
        response.setUnit(savedProduct.getUnit());
        response.setStock(savedProduct.getStock());
        response.setIsOrganic(savedProduct.getIsOrganic());
        response.setPrice(savedProduct.getPrice());
        response.setOriginalPrice(savedProduct.getOriginalPrice());
        response.setVendorId(savedProduct.getVendorId().toHexString());
        return response;
    }

    private Product mapRequestToProduct(ProductRequest request,ObjectId vendorId) {
        Product product = new Product();

        product.setVendorId(vendorId);
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setImage(request.getImage());
        product.setCategory(request.getCategory());
        product.setUnit(request.getUnit());
        product.setStock(request.getStock());
        product.setIsOrganic(request.getIsOrganic());
        return product;
    }



}