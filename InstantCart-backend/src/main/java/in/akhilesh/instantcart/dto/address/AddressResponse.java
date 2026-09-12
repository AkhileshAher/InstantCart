package in.akhilesh.instantcart.dto.address;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AddressResponse {
    private String id;
    private String userId;
    private String label;
    private String address;
    private String city;
    private String state;
    private String zip;
    private Double lat;
    private Double lng;
    private Boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
