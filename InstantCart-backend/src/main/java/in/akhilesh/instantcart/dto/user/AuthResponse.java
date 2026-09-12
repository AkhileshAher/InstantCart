package in.akhilesh.instantcart.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;


@Data
@AllArgsConstructor
public class AuthResponse {
    private ObjectId userId;
    private String username;
    private String email;
    private String accessToken;
}
