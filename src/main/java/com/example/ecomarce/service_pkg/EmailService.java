package com.example.ecomarce.service_pkg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;



    public void sendVerificationEmail(String to, String link) {

        System.out.println("Sending verification email to " + to +"   " +link);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Email Verification");
        message.setText("Dear User,\n\nPlease use the following code to verify your email: " + link + "\n\nThank you!");
        message.setFrom("ah.java@fahim-khan-96.org"); // Must match spring.mail.username

        try {
            mailSender.send(message);

            System.out.println("Verification email sent to: " + to);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}