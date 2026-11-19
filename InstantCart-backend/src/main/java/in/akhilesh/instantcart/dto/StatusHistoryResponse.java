package in.akhilesh.instantcart.dto;

import in.akhilesh.instantcart.entity.OrderStatus;
import in.akhilesh.instantcart.entity.OrderStatusHistory;

import java.time.LocalDateTime;

public record StatusHistoryResponse(OrderStatus status, String note, LocalDateTime changedAt) {
        public static StatusHistoryResponse from(OrderStatusHistory h) {
            return new StatusHistoryResponse(h.getStatus(), h.getNote(), h.getChangedAt());
        }
    }