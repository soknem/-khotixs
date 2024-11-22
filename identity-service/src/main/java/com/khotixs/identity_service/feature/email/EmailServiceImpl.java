package com.khotixs.identity_service.feature.email;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setFrom("your-email@gmail.com"); // Use your Gmail address here
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // Set to true to support HTML emails
            
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Log or handle the exception
        }
    }
}
