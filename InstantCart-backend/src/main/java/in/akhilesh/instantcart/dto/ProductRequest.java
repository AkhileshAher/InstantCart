package in.akhilesh.instantcart.dto;

import jakarta.validation.constraints.*;

public record ProductRequest(
            @NotNull Long categoryId,
            @NotBlank String name,
            String description,
            @NotNull @Positive Double price,
            Double originalPrice,
            @Min(0) @Max(100) Integer discountPercent,
            String image,
            String unit,
            @PositiveOrZero Integer stock,
            Boolean isOrganic) {}
