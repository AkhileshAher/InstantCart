package in.akhilesh.instantcart.dto;

import in.akhilesh.instantcart.entity.DeliveryPartner;

public record DeliveryPartnerResponse(
            Long id, String name, String email, String phone,
            String avatar, Boolean isActive) {

        public static DeliveryPartnerResponse from(DeliveryPartner d) {
            return new DeliveryPartnerResponse(d.getId(), d.getName(), d.getEmail(), d.getPhone(),
                    d.getAvatar(), d.getIsActive());
        }
    }