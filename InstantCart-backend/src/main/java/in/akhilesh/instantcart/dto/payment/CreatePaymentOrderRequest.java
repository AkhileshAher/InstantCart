package in.akhilesh.instantcart.dto.payment;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class CreatePaymentOrderRequest {
    private List<PaymentCartItem> items;
}
