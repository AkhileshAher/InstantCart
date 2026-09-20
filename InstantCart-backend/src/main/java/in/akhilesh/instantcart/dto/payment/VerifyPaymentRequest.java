package in.akhilesh.instantcart.dto.payment;

import in.akhilesh.instantcart.dto.order.CreateOrderRequest;
import lombok.Data;

@Data
public class VerifyPaymentRequest{
        private String razorpay_payment_id;
        private String razorpay_order_id;
        private String razorpay_signature;
        private CreateOrderRequest orderRequest;
}