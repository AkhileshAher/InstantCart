package in.akhilesh.instantcart.controller;

import in.akhilesh.instantcart.dto.product.ProductRequest;
import in.akhilesh.instantcart.dto.product.ProductResponse;
import in.akhilesh.instantcart.security.JwtPrincipal;
import in.akhilesh.instantcart.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/popular")
    public ResponseEntity<List<ProductResponse>> popularProducts(@RequestParam Integer limit) {
        List<ProductResponse> productResponses = productService.popularProducts(limit);
        return ResponseEntity.ok(productResponses);
    }


    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String q) {
        return ResponseEntity.ok(productService.searchProducts(q));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sort
    ) {

        List<ProductResponse> products = productService.getProducts(
                sort,
                category,
                minPrice,
                maxPrice
        );
        return ResponseEntity.ok(products);
    }

    @GetMapping("/flash-deals")
    public ResponseEntity<List<ProductResponse>> getFlashDeals() {

        List<ProductResponse> flashDeals = productService.getFlashDeals();
        return ResponseEntity.ok(flashDeals);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable ObjectId productId) {
        ProductResponse product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(
            Authentication authentication,
            @RequestBody @Valid ProductRequest request,
            @RequestParam(value = "image",required = false)MultipartFile file
            ) {

        JwtPrincipal principal =
                (JwtPrincipal) authentication.getPrincipal();
        ObjectId vendorId = principal.getUserId();

        ProductResponse product = productService.addProduct(vendorId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(product);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable ObjectId productId,
            Authentication authentication,
            @RequestBody ProductRequest request
    ) {

        JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();
        ObjectId vendorId = principal.getUserId();

        ProductResponse updatedProduct = productService.updateProduct(productId, vendorId, request);

        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable ObjectId productId,
            Authentication authentication
    ) {

        JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();
        ObjectId vendorId = principal.getUserId();

        productService.deleteProduct(
                productId,
                vendorId
        );

        return ResponseEntity.noContent().build();
    }
}