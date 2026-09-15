package in.akhilesh.instantcart.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    @NotEmpty(message = "Items cannot be Empty")
    @Valid
    private List<OrderItemRequest> items;

    @Valid
    @NotNull
    private ShippingAddressRequest shippingAddress;

    @NotBlank(message = "Payment method required")
    private String paymentMethod;

}
