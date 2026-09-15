package in.akhilesh.instantcart.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ShippingAddressRequest {
    @NotBlank(message = "Label is Required")
    private String label;
    @NotBlank(message = "Address is Required")
    private String address;
    @NotBlank(message = "City is Required")
    private String city;
    @NotBlank(message = "State is Required")
    private String state;
    @NotBlank(message = "Zip code is Required")
    private String zip;
    private Double lat;
    private Double lng;
}
