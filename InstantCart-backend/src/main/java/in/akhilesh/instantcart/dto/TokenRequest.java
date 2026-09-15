package in.akhilesh.instantcart.dto;

import in.akhilesh.instantcart.entity.enums.UserRole;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class TokenRequest {
    private ObjectId id;

    private String name;

    private String email;

    private UserRole role;
}
