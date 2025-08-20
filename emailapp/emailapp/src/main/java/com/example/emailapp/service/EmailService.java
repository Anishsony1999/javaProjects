package com.example.emailapp.service;

import com.example.emailapp.controller.EmailController.EmailStats;
import com.example.emailapp.dto.EmailResponse;
import com.example.emailapp.model.Email;
import com.example.emailapp.repository.EmailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private EmailRepository emailRepository;

    public Page<EmailResponse> getAllEmails(Pageable pageable) {
        Page<Email> emails = emailRepository.findAll(pageable);
        return emails.map(this::convertToResponse);
    }

    public EmailResponse getEmailById(Long id) {
        Email email = emailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Email not found with ID: " + id));
        return convertToResponse(email);
    }

    public Page<EmailResponse> searchEmails(String keyword, Pageable pageable) {
        Page<Email> emails = emailRepository.searchEmails(keyword, pageable);
        return emails.map(this::convertToResponse);
    }

    public List<EmailResponse> getUnreadEmails() {
        List<Email> unreadEmails = emailRepository.findByIsRead(false);
        return unreadEmails.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<EmailResponse> getEmailsBySender(String sender) {
        List<Email> emails = emailRepository.findBySenderContainingIgnoreCase(sender);
        return emails.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public void markAsRead(Long id) {
        Email email = emailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Email not found with ID: " + id));
        email.setIsRead(true);
        emailRepository.save(email);
        logger.info("Email {} marked as read", id);
    }

    public void markAsUnread(Long id) {
        Email email = emailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Email not found with ID: " + id));
        email.setIsRead(false);
        emailRepository.save(email);
        logger.info("Email {} marked as unread", id);
    }

    public void deleteEmail(Long id) {
        if (!emailRepository.existsById(id)) {
            throw new IllegalArgumentException("Email not found with ID: " + id);
        }
        emailRepository.deleteById(id);
        logger.info("Email {} deleted successfully", id);
    }

    public EmailStats getEmailStats() {
        long totalEmails = emailRepository.count();
        long unreadEmails = emailRepository.countUnreadEmails();
        long readEmails = totalEmails - unreadEmails;

        return new EmailStats(totalEmails, unreadEmails, readEmails);
    }

    private EmailResponse convertToResponse(Email email) {
        return new EmailResponse(
                email.getId(),
                email.getMessageId(),
                email.getSender(),
                email.getRecipients(),
                email.getSubject(),
                email.getContent(),
                email.getSentDate(),
                email.getReceivedDate(),
                email.getIsRead(),
                email.getFolder(),
                email.getHasAttachments()
        );
    }
}