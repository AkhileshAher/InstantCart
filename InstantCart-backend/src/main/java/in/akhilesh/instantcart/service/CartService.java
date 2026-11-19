package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.AddToCartRequest;
import in.akhilesh.instantcart.dto.CartItemResponse;
import in.akhilesh.instantcart.dto.UpdateQuantityRequest;
import in.akhilesh.instantcart.entity.CartItem;
import in.akhilesh.instantcart.entity.Product;
import in.akhilesh.instantcart.entity.User;
import in.akhilesh.instantcart.repository.CartItemRepository;
import in.akhilesh.instantcart.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepo;
    private final ProductRepository productRepo;

    public List<CartItemResponse> createCart(User user, @Valid AddToCartRequest req) {
        Product product = activeProduct(req.productId());

        CartItem item = cartItemRepo.findByUserIdAndProductId(user.getId(), product.getId())
                .orElseGet(() -> {
                    CartItem c = new CartItem();
                    c.setUser(user);
                    c.setProduct(product);
                    c.setQuantity(0);
                    return c;
                });

        item.setQuantity(checkedQuantity(product, item.getQuantity() + req.quantity()));
        cartItemRepo.save(item);
        return cartOf(user);
    }

    public List<CartItemResponse> getCartItem(User user) {
        return cartOf(user);
    }

    public List<CartItemResponse> updateQuantity(User user, Long productId, @Valid UpdateQuantityRequest req) {
        CartItem item = itemOf(user, productId);
        item.setQuantity(checkedQuantity(item.getProduct(), req.quantity()));
        cartItemRepo.save(item);
        return cartOf(user);
    }

    public List<CartItemResponse> deleteCartItem(User user, Long productId) {
        cartItemRepo.delete(itemOf(user, productId));
        return cartOf(user);
    }

    public void clearCartItems(User user) {
        cartItemRepo.deleteAll(cartItemRepo.findByUserId(user.getId()));
    }

    private List<CartItemResponse> cartOf(User user) {
        return cartItemRepo.findByUserId(user.getId()).stream().map(CartItemResponse::from).toList();
    }

    private CartItem itemOf(User user, Long productId) {
        return cartItemRepo.findByUserIdAndProductId(user.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Cart item Not Found"));
    }

    private Product activeProduct(Long productId) {
        return productRepo.findById(productId)
                .filter(Product::getIsActive)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    private int checkedQuantity(Product product, int quantity) {
        if (quantity > product.getStock()) {
            throw new RuntimeException("Only " + product.getStock() + " in stock");
        }
        return quantity;
    }
}
