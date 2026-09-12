package in.akhilesh.instantcart.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(collection = "delivery_partners")
public class DeliveryPartner {

    @Id
    private ObjectId id;

    private String name;

    private String email;

    private String password;

    private String phone;

    private String avatar = "";

    private String vehicleType = "bike";

    private Boolean isActive = true;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}