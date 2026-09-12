package in.akhilesh.instantcart.dto.order;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.UUID;

@Data
public class OrderItemRequest {
    private ObjectId productId;
    private Integer quantity;
}
