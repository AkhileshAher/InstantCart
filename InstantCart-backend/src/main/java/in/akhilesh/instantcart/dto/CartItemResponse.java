package in.akhilesh.instantcart.dto;

import in.akhilesh.instantcart.entity.CartItem;
import in.akhilesh.instantcart.entity.Product;

public record CartItemResponse(
            Long productId, String name, String image, Double price,
            Integer discountPercent, Integer quantity) {

        public static CartItemResponse from(CartItem c) {
            Product p = c.getProduct();
            return new CartItemResponse(p.getId(), p.getName(), p.getImage(), p.getPrice(),
                    p.getDiscountPercent(), c.getQuantity());
        }
    }
