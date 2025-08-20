package com.example.emailapp.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailTemplateService {

    public String generateWelcomeEmail(String recipientName) {
        return String.format(
                "<html><body>" +
                        "<h2>Welcome %s!</h2>" +
                        "<p>Thank you for joining our email application.</p>" +
                        "<p>We're excited to have you on board!</p>" +
                        "<br/>" +
                        "<p>Best regards,<br/>The Email App Team</p>" +
                        "</body></html>",
                recipientName
        );
    }

    public String generateNotificationEmail(String title, String message) {
        return String.format(
                "<html><body>" +
                        "<h3>%s</h3>" +
                        "<p>%s</p>" +
                        "<br/>" +
                        "<p><small>This is an automated notification from Email App.</small></p>" +
                        "</body></html>",
                title, message
        );
    }

    public String processTemplate(String template, Map<String, String> placeholders) {
        String result = template;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return result;
    }

    public String generatePasswordResetEmail(String recipientName, String resetLink) {
        return String.format(
                "<html><body>" +
                        "<h2>Password Reset Request</h2>" +
                        "<p>Hello %s,</p>" +
                        "<p>We received a request to reset your password. Click the link below to reset it:</p>" +
                        "<p><a href=\"%s\">Reset Password</a></p>" +
                        "<p>If you didn't request this, please ignore this email.</p>" +
                        "<br/>" +
                        "<p>Best regards,<br/>The Email App Team</p>" +
                        "</body></html>",
                recipientName, resetLink
        );
    }
}