package in.akhilesh.instantcart.dto.user;

import in.akhilesh.instantcart.dto.delivery.DeliveryPartnerResponse;
import in.akhilesh.instantcart.entity.DeliveryPartner;
import lombok.Data;


@Data
public class AuthServiceResponse {
    private String accessToken;
    private UserResponse user;
    private DeliveryPartnerResponse partner;
}
