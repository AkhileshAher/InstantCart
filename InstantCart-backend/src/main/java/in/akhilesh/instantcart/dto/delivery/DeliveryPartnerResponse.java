package in.akhilesh.instantcart.dto.delivery;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeliveryPartnerResponse {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String avatar = "";
    private String vehicleType = "bike";
    private Boolean isActive = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
