package com.example.emailapp.service;

import com.example.emailapp.dto.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailSendingService {

    private static final Logger logger = LoggerFactory.getLogger(EmailSendingService.class);

    @Autowired
    private JavaMailSender mailSender;
    private EmailRequest emailRequest;

    public void sendSimpleEmail(EmailRequest emailRequest) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailRequest.getTo().toArray(new String[0]));

        if (emailRequest.getCc() != null && !emailRequest.getCc().isEmpty()) {
            message.setCc(emailRequest.getCc().toArray(new String[0]));
        }

        if (emailRequest.getBcc() != null && !emailRequest.getBcc().isEmpty()) {
            message.setBcc(emailRequest.getBcc().toArray(new String[0]));
        }

        message.setSubject(emailRequest.getSubject());
        message.setText(emailRequest.getContent());

        mailSender.send(message);
        logger.info("Simple email sent successfully to: {}", String.join(", ", emailRequest.getTo()));
    }

    public void sendMimeEmail(EmailRequest emailRequest) throws MessagingException {
        this.emailRequest = emailRequest;
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(emailRequest.getTo().toArray(new String[0]));

        if (emailRequest.getCc() != null && !emailRequest.getCc().isEmpty()) {
            helper.setCc(emailRequest.getCc().toArray(new String[0]));
        }

        if (emailRequest.getBcc() != null && !emailRequest.getBcc().isEmpty()) {
            helper.setBcc(emailRequest.getBcc().toArray(new String[0]));
        }

        helper.setSubject(emailRequest.getSubject());
        helper.setText(emailRequest.getContent(), emailRequest.isHtml());

        // Add attachments if provided
        if (emailRequest.getAttachmentPaths() != null && !emailRequest.getAttachmentPaths().isEmpty()) {
            for (String attachmentPath : emailRequest.getAttachmentPaths()) {
                FileSystemResource file = new FileSystemResource(new File(attachmentPath));
                if (file.exists()) {
                    helper.addAttachment(file.getFilename(), file);
                } else {
                    logger.warn("Attachment file not found: {}", attachmentPath);
                }
            }
        }

        mailSender.send(message);
        logger.info("MIME email sent successfully to: {}", String.join(", ", emailRequest.getTo()));
    }

    @Async
    public CompletableFuture<Void> sendEmailAsync(EmailRequest emailRequest) {
        try {
            if (emailRequest.isHtml() ||
                    (emailRequest.getAttachmentPaths() != null && !emailRequest.getAttachmentPaths().isEmpty())) {
                sendMimeEmail(emailRequest);
            } else {
                sendSimpleEmail(emailRequest);
            }
            logger.info("Async email sent successfully");
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            logger.error("Failed to send async email", e);
            return CompletableFuture.failedFuture(e);
        }
    }
}