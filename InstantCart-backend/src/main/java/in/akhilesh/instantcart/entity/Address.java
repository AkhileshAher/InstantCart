package in.akhilesh.instantcart.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(collection = "addresses")
public class Address {

    @Id
    private ObjectId id;

    private ObjectId userId;

    private String label;

    private String address;

    private String city;

    private String state;

    private String zip;

    private Boolean isDefault = false;

    private Double lat;

    private Double lng;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}