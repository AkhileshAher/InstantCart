package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.OrderEmailData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;


    public void sendMail(String to, String subject, OrderEmailData emailData) {
        String body = """
        Hello InstantCart User,

        Welcome to InstantCart!

        Your OTP is: %s

        Order Details
        -------------------------
        Order ID: %s
        Order Date: %s
        Order Status: %s

        Items:
        %s

        Subtotal: ₹%.2f
        Delivery Fee: ₹%.2f
        Total Amount: ₹%.2f

        Delivery Address:
        %s

        Thank you for choosing InstantCart!

        InstantCart Team
        """.formatted(
                emailData.getOtp(),
                emailData.getOrderId(),
                emailData.getOrderDate(),
                emailData.getOrderStatus(),
                emailData.getOrderItems(),
                emailData.getSubtotal(),
                emailData.getDeliveryFee(),
                emailData.getTotalAmount(),
                emailData.getDeliveryAddress()
        );
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);
            javaMailSender.send(mail);
        } catch (Exception e) {
            log.error("Failed to send email to {} (subject: {}): {}", to, subject, e.getMessage(), e);
        }
    }

}
