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
@Document(collection = "products")
public class Product {

    @Id
    private ObjectId id;

    private ObjectId vendorId;

    private String name;

    private String description = "";

    private Double price;

    private Double originalPrice = 0.0;

    private Integer discount;

    private String image;

    private String category;

    private String unit = "piece";

    private Integer stock = 0;

    private Boolean isOrganic = false;

    private Float rating = 0f;

    private Integer reviewCount = 0;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}