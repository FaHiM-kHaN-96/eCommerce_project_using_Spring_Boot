package com.example.ecomarce.service_pkg;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    public void sendThankYouEmail(String to, String customerName, String invoice_id) {
        String subject = "Thank You for Your Order!";

        String htmlContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { color: #2c3e50; font-size: 24px; border-bottom: 2px solid #f8f9fa; padding-bottom: 10px; }" +
                "        .content { margin: 20px 0; }" +
                "        .footer { margin-top: 30px; font-size: 14px; color: #7f8c8d; border-top: 1px solid #f8f9fa; padding-top: 10px; }" +
                "        .button { background-color: #3498db; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block; }" +
                "        .order-details { background: #f8f9fa; padding: 15px; border-radius: 5px; margin: 20px 0; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='header'>Thank You, " + customerName + "!</div>" +
                "    <div class='content'>" +
                "        <p>We truly appreciate your business and are processing your order <strong>#" + invoice_id + "</strong>.</p>" +
                "        <p>You'll receive another email when your order ships with tracking information.</p>" +
                "        <div class='order-details'>" +
                "            <p><strong>Need help with your order?</strong></p>" +
                "            <p>Our customer service team is ready to assist you with any questions.</p>" +
                "            <a href='mailto:support@yourcompany.com' class='button'>Contact Support</a>" +
                "        </div>" +
                "        <p>Thank you for choosing us!</p>" +
                "    </div>" +
                "    <div class='footer'>" +
                "        <p>Best regards,<br>The [Your Company] Team</p>" +
                "        <p><small>© 2023 Your Company. All rights reserved.</small></p>" +
                "    </div>" +
                "</body>" +
                "</html>";

        System.out.println("Sending thank you email to " + customerName + " at " + to);

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indicates HTML
            helper.setFrom("ah.java@fahim-khan-96.org"); // Must match spring.mail.username

            mailSender.send(message);
            System.out.println("Thank you email sent to: " + to);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }



    public void sendRemoveDeviceVerificationEmail(String to, String verificationLink) {
        String subject = "Verify Your Email Address";

        String htmlContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { color: #2c3e50; font-size: 22px; font-weight: bold; margin-bottom: 20px; }" +
                "        .content { margin: 20px 0; }" +
                "        .button { background-color: #3498db; color: white; padding: 12px 20px; text-decoration: none; border-radius: 5px; display: inline-block; font-size: 16px; }" +
                "        .footer { margin-top: 30px; font-size: 14px; color: #7f8c8d; border-top: 1px solid #f1f1f1; padding-top: 10px; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='header'>Email Verification</div>" +
                "    <div class='content'>" +
                "        <p>Hello,</p>" +
                "        <p>Thank you for registering. Please verify your email address by clicking the button below:</p>" +
                "        <p><a href='" + verificationLink + "' class='button'>Verify Email</a></p>" +
                "        <p>If the button doesn’t work, copy and paste this link into your browser:</p>" +
                "        <p><a href='" + verificationLink + "'>" + verificationLink + "</a></p>" +
                "    </div>" +
                "    <div class='footer'>" +
                "        <p>If you didn’t request this, please ignore this email.</p>" +
                "        <p>Best regards,<br>The [Your Company] Team</p>" +
                "    </div>" +
                "</body>" +
                "</html>";

        System.out.println("Sending verification email to " + to + " with link " + verificationLink);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // ✅ Send as HTML
            helper.setFrom("ah.java@fahim-khan-96.org"); // Must match spring.mail.username

            mailSender.send(message);

            System.out.println("Verification email sent to: " + to);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send verification email: " + e.getMessage());
        }
    }
}