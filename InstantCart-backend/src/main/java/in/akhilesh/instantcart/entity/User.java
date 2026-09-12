package in.akhilesh.instantcart.entity;

import in.akhilesh.instantcart.entity.enums.UserRole;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(collection = "users")
public class User {

    @Id
    private ObjectId id;

    private String name;

    private String email;

    private String password;

    private UserRole role = UserRole.USER;

    private String phone = "";

    private String avatar = "";

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}