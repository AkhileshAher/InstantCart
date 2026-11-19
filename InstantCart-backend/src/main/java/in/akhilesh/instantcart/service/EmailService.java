package in.akhilesh.instantcart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final MailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {

        String mailMessage = String.format("""
                Hello,
                
                Thank you for registering with InstantCart!
                
                To verify your email address, please use the following One-Time Password (OTP):
                
                {{OTP}}
                
                This OTP is valid for 5 minutes. Please do not share it with anyone.
                
                If you did not request this verification, you can safely ignore this email.
                
                Happy Shopping!
                
                Team InstantCart""", otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Verify Your Email – InstantCart");
        message.setText(mailMessage);
        mailSender.send(message);
    }

}
