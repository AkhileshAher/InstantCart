package in.akhilesh.instantcart.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DeliveryPartnerRequest(
            @NotBlank String name,
            @NotBlank @Email String email,
            String password,
            String phone,
            String vehicleType) {}