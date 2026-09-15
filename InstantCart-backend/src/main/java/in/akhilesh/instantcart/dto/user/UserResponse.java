package in.akhilesh.instantcart.dto.user;

import in.akhilesh.instantcart.entity.enums.UserRole;
import lombok.Data;
import org.bson.types.ObjectId;
import java.time.LocalDateTime;

@Data
public class UserResponse {
    private ObjectId id;
    private String name;
    private String email;
    private UserRole role;
    private String phone = "";
    private String avatar = "";
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
