package in.akhilesh.instantcart.dto.user;

import in.akhilesh.instantcart.entity.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class AuthRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email Address")
    private String email;

    @NotBlank(message = "Password is Required")
    @Size(min = 8, message = "Password Must Contain Atleast 8 characters")
    private String password;

    @NotNull(message = "UserRole is Required")
    private UserRole role;

    @Pattern(regexp = "^\\+91[6-9]\\d{9}$", message = "Phone number must be a valid Indian number starting with +91")
    private String phone;

    private String avatar;
}
