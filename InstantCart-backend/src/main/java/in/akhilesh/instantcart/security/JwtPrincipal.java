package in.akhilesh.instantcart.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class JwtPrincipal {

    private ObjectId userId;
    private String name;
    private String email;
    private String role;
}