package in.akhilesh.instantcart.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
            String label,
            @NotBlank String addressLine,
            @NotBlank String city,
            String state,
            @NotBlank String zip,
            Double lat,
            Double lng,
            Boolean isDefault) {}
