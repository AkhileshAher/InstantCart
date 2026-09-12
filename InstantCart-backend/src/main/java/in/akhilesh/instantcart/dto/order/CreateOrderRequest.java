package in.akhilesh.instantcart.dto.order;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private List<OrderItemRequest> items;

    private ShippingAddressRequest shippingAddress;

    private String paymentMethod;

}
