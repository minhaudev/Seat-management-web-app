package sourse.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    @Autowired
    JavaMailSender mailSender;
    public void sendResetPasswordEmail(String toEmail, String token) {
        String resetLink = "http://localhost:3000/resetpassword/"+token;
        String subject = "Reset your password";
        String body = "Click the link to reset your password: \n" + resetLink;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("minhhau2803@gmail.com");
        mailSender.send(message);
    }

}
