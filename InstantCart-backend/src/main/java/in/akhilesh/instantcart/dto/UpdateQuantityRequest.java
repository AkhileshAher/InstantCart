package in.akhilesh.instantcart.dto;

import jakarta.validation.constraints.Positive;

public record UpdateQuantityRequest(@Positive int quantity) {}

