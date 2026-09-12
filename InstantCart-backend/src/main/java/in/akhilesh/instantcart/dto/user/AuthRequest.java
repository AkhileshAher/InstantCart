package in.akhilesh.instantcart.dto.user;

import in.akhilesh.instantcart.entity.enums.UserRole;
import lombok.Data;

import java.util.UUID;

@Data
public class AuthRequest {
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private String phone;
    private String avatar;
}
