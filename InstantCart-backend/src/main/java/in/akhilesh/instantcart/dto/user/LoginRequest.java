package in.akhilesh.instantcart.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;


@Data
public class LoginRequest {
    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid Email Format")
    private String email;

    @NotBlank(message = "Password is Required")
    @Size(min = 8, message = "Password Must Contain Atleast 8 characters")
    private String password;
}
