package in.akhilesh.instantcart.dto.address;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressRequest {

    @NotBlank(message = "Label is Required")
    private String label;

    @NotBlank(message = "Address is Required")
    private String address;

    @NotBlank(message = "City is Required")
    private String city;

    @NotBlank(message = "State is Required")
    private String state;

    @NotBlank(message = "Zip Code is Required")
    private String zip;

    private Boolean isDefault;

    private Double lat;

    private Double lng;
}
