package in.akhilesh.instantcart.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderEmailData {

    private String otp;

    private String orderId;
    private String orderDate;
    private String orderStatus;

    private String orderItems;
    private double subtotal;
    private double deliveryFee;
    private double totalAmount;

    private String deliveryAddress;
}