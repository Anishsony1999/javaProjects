package com.example.emailapp.controller;

import com.example.emailapp.dto.ApiResponse;
import com.example.emailapp.dto.EmailRequest;
import com.example.emailapp.dto.EmailResponse;
import com.example.emailapp.model.Email;
import com.example.emailapp.service.EmailSendingService;
import com.example.emailapp.service.EmailService;
import com.example.emailapp.service.EmailReadingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/emails")
@CrossOrigin(origins = "*")
public class EmailController {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private EmailSendingService emailSendingService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailReadingService emailReadingService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<String>> sendEmail(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            if (emailRequest.isHtml() ||
                    (emailRequest.getAttachmentPaths() != null && !emailRequest.getAttachmentPaths().isEmpty())) {
                emailSendingService.sendMimeEmail(emailRequest);
            } else {
                emailSendingService.sendSimpleEmail(emailRequest);
            }

            logger.info("Email sent successfully to: {}", String.join(", ", emailRequest.getTo()));
            return ResponseEntity.ok(ApiResponse.success("Email sent successfully"));

        } catch (Exception e) {
            logger.error("Failed to send email", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to send email: " + e.getMessage()));
        }
    }

    @PostMapping("/send-async")
    public ResponseEntity<ApiResponse<String>> sendEmailAsync(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            CompletableFuture<Void> future = emailSendingService.sendEmailAsync(emailRequest);

            logger.info("Async email queued for sending to: {}", String.join(", ", emailRequest.getTo()));
            return ResponseEntity.ok(ApiResponse.success("Email queued for sending"));

        } catch (Exception e) {
            logger.error("Failed to queue email for sending", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to queue email: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<EmailResponse>>> getAllEmails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "sentDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<EmailResponse> emails = emailService.getAllEmails(pageable);

            return ResponseEntity.ok(ApiResponse.success(emails));

        } catch (Exception e) {
            logger.error("Failed to retrieve emails", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve emails: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmailResponse>> getEmailById(@PathVariable Long id) {
        try {
            EmailResponse email = emailService.getEmailById(id);
            return ResponseEntity.ok(ApiResponse.success(email));

        } catch (Exception e) {
            logger.error("Failed to retrieve email with ID: {}", id, e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Email not found"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<EmailResponse>>> searchEmails(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("sentDate").descending());
            Page<EmailResponse> emails = emailService.searchEmails(keyword, pageable);

            return ResponseEntity.ok(ApiResponse.success(emails));

        } catch (Exception e) {
            logger.error("Failed to search emails with keyword: {}", keyword, e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to search emails: " + e.getMessage()));
        }
    }

    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<EmailResponse>>> getUnreadEmails() {
        try {
            List<EmailResponse> unreadEmails = emailService.getUnreadEmails();
            return ResponseEntity.ok(ApiResponse.success(unreadEmails));

        } catch (Exception e) {
            logger.error("Failed to retrieve unread emails", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve unread emails: " + e.getMessage()));
        }
    }

    @GetMapping("/sender/{sender}")
    public ResponseEntity<ApiResponse<List<EmailResponse>>> getEmailsBySender(@PathVariable String sender) {
        try {
            List<EmailResponse> emails = emailService.getEmailsBySender(sender);
            return ResponseEntity.ok(ApiResponse.success(emails));

        } catch (Exception e) {
            logger.error("Failed to retrieve emails from sender: {}", sender, e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve emails from sender: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/mark-read")
    public ResponseEntity<ApiResponse<String>> markAsRead(@PathVariable Long id) {
        try {
            emailService.markAsRead(id);
            return ResponseEntity.ok(ApiResponse.success("Email marked as read"));

        } catch (Exception e) {
            logger.error("Failed to mark email as read: {}", id, e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to mark email as read"));
        }
    }

    @PutMapping("/{id}/mark-unread")
    public ResponseEntity<ApiResponse<String>> markAsUnread(@PathVariable Long id) {
        try {
            emailService.markAsUnread(id);
            return ResponseEntity.ok(ApiResponse.success("Email marked as unread"));

        } catch (Exception e) {
            logger.error("Failed to mark email as unread: {}", id, e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to mark email as unread"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteEmail(@PathVariable Long id) {
        try {
            emailService.deleteEmail(id);
            return ResponseEntity.ok(ApiResponse.success("Email deleted successfully"));

        } catch (Exception e) {
            logger.error("Failed to delete email: {}", id, e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete email"));
        }
    }

    @PostMapping("/check-mail")
    public ResponseEntity<ApiResponse<String>> checkMail() {
        try {
            emailReadingService.manualEmailCheck();
            return ResponseEntity.ok(ApiResponse.success("Email check initiated"));

        } catch (Exception e) {
            logger.error("Failed to check mail", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to check mail: " + e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<EmailStats>> getEmailStats() {
        try {
            EmailStats stats = emailService.getEmailStats();
            return ResponseEntity.ok(ApiResponse.success(stats));

        } catch (Exception e) {
            logger.error("Failed to retrieve email stats", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve stats: " + e.getMessage()));
        }
    }

    // Inner class for email statistics
    public static class EmailStats {
        private long totalEmails;
        private long unreadEmails;
        private long readEmails;

        public EmailStats(long totalEmails, long unreadEmails, long readEmails) {
            this.totalEmails = totalEmails;
            this.unreadEmails = unreadEmails;
            this.readEmails = readEmails;
        }

        // Getters and Setters
        public long getTotalEmails() { return totalEmails; }
        public void setTotalEmails(long totalEmails) { this.totalEmails = totalEmails; }

        public long getUnreadEmails() { return unreadEmails; }
        public void setUnreadEmails(long unreadEmails) { this.unreadEmails = unreadEmails; }

        public long getReadEmails() { return readEmails; }
        public void setReadEmails(long readEmails) { this.readEmails = readEmails; }
    }
}