package in.akhilesh.instantcart.dto;

import in.akhilesh.instantcart.entity.Category;
import in.akhilesh.instantcart.entity.Product;

public record ProductResponse(
            Long id, Long categoryId, String categoryName, String name, String description,
            Double price, Double originalPrice, Integer discountPercent, String unit,
            Integer stock, String image, Boolean isOrganic, Boolean isActive,
            Float rating, Integer reviewCount) {

        public static ProductResponse from(Product p) {
            Category c = p.getCategory();
            return new ProductResponse(
                    p.getId(),
                    c == null ? null : c.getId(),
                    c == null ? null : c.getName(),
                    p.getName(), p.getDescription(), p.getPrice(), p.getOriginalPrice(),
                    p.getDiscountPercent(), p.getUnit(), p.getStock(), p.getImage(),
                    p.getIsOrganic(), p.getIsActive(), p.getRating(), p.getReviewCount());
        }
    }
