package in.akhilesh.instantcart.dto;

import in.akhilesh.instantcart.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PlaceOrderRequest(@NotNull Long addressId, @NotNull PaymentMethod paymentMethod) {}
