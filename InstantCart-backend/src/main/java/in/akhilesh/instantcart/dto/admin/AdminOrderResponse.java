package in.akhilesh.instantcart.dto.admin;

import in.akhilesh.instantcart.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AdminOrderResponse {
    private ObjectId orderId;

    private String customerName;

    private Double total;

    private OrderStatus status;

    private ObjectId deliveryPartnerId;

    private String deliveryPartnerName;

    private LocalDateTime createdAt;
}
