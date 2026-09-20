package in.akhilesh.instantcart.dto.payment;

import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;

import java.util.UUID;

@Data
@ToString
public class PaymentCartItem {

    private ObjectId productId;
    private int quantity;
}