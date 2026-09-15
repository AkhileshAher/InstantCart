package in.akhilesh.instantcart.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
public class RememberResponse {
    private String userId;
    private String name;
    private String email;
    private String role;
}
