package in.akhilesh.instantcart.dto.delivery;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DeliverPartnerRequest {

    @NotBlank(message = "Name is Required")
    private String name;

    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid Email Format")
    private String email;

    @NotBlank(message = "Password is Required")
    private String password;

    @Pattern(regexp = "^\\+91[6-9]\\d{9}$", message = "Phone number must be a valid Indian number starting with +91")
    private String phone;

    private String avatar = "";

    private String vehicleType = "bike";

    private Boolean isActive = true;

}
