package in.akhilesh.instantcart.controller;

import in.akhilesh.instantcart.dto.AddToCartRequest;
import in.akhilesh.instantcart.dto.CartItemResponse;
import in.akhilesh.instantcart.dto.UpdateQuantityRequest;
import in.akhilesh.instantcart.entity.User;
import in.akhilesh.instantcart.payload.ApiResponse;
import in.akhilesh.instantcart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {


    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> get(@AuthenticationPrincipal User user) {
        List<CartItemResponse> cartItem = cartService.getCartItem(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Cart Items fetched Successfully",
                        cartItem
                ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> add(@AuthenticationPrincipal User user,
                                      @Valid @RequestBody AddToCartRequest req) {
        List<CartItemResponse> cart = cartService.createCart(user, req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.CREATED.value(),
                        "Cart Items created Successfully",
                        cart
                ));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> updateQuantity(@AuthenticationPrincipal User user,
                                                 @PathVariable Long productId,
                                                 @Valid @RequestBody UpdateQuantityRequest req) {
        List<CartItemResponse> cartItemResponses = cartService.updateQuantity(user, productId, req);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Cart Items updated Successfully",
                        cartItemResponses
                ));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> remove(@AuthenticationPrincipal User user, @PathVariable Long productId) {
        List<CartItemResponse> deletedCartItem = cartService.deleteCartItem(user, productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Cart Items deleted Successfully",
                        deletedCartItem
                ));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Object>> clear(@AuthenticationPrincipal User user) {
        cartService.clearCartItems(user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.NO_CONTENT.value(),
                        "Cart cleared successfully",
                        null
                ));
    }


}
