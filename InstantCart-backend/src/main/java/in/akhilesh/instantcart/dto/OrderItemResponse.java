package in.akhilesh.instantcart.dto;

import in.akhilesh.instantcart.entity.OrderItem;

public record OrderItemResponse(
            Long productId, String name, Double unitPrice,
            Integer discountPercent, Integer quantity, Double lineTotal) {

        public static OrderItemResponse from(OrderItem i) {
            return new OrderItemResponse(
                    i.getProduct() == null ? null : i.getProduct().getId(),
                    i.getProductName(), i.getUnitPrice(), i.getDiscountPercent(),
                    i.getQuantity(), i.getLineTotal());
        }
    }