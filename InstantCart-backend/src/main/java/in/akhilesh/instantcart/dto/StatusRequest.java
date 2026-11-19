package in.akhilesh.instantcart.dto;

import in.akhilesh.instantcart.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record StatusRequest(@NotNull OrderStatus status, String note) {}
