package in.akhilesh.instantcart.dto.order;

import in.akhilesh.instantcart.entity.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderStatusHistory {

    private OrderStatus status;

    private LocalDateTime changedAt;
}