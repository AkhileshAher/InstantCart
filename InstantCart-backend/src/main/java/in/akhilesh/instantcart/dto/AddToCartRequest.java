package in.akhilesh.instantcart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddToCartRequest(@NotNull Long productId, @Positive int quantity) {}
