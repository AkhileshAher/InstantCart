package in.akhilesh.instantcart.dto.payment;

import java.util.UUID;

public record VerifyPaymentResponse(
        String status,
        String message
) {
}