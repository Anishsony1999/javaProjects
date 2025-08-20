package com.example.emailapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "emails")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_id", unique = true)
    private String messageId;

    @Column(name = "sender", nullable = false)
    private String sender;

    @Column(name = "recipients", columnDefinition = "TEXT")
    private String recipients; // Stored as comma-separated values

    @Column(name = "subject")
    private String subject;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "sent_date")
    private LocalDateTime sentDate;

    @Column(name = "received_date")
    private LocalDateTime receivedDate;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "folder")
    private String folder;

    @Column(name = "has_attachments")
    private Boolean hasAttachments = false;

    // Constructors
    public Email() {}

    public Email(String messageId, String sender, String recipients, String subject,
                 String content, LocalDateTime sentDate) {
        this.messageId = messageId;
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.content = content;
        this.sentDate = sentDate;
        this.receivedDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getRecipients() { return recipients; }
    public void setRecipients(String recipients) { this.recipients = recipients; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public LocalDateTime getSentDate() { return sentDate; }
    public void setSentDate(LocalDateTime sentDate) { this.sentDate = sentDate; }

    public LocalDateTime getReceivedDate() { return receivedDate; }
    public void setReceivedDate(LocalDateTime receivedDate) { this.receivedDate = receivedDate; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public String getFolder() { return folder; }
    public void setFolder(String folder) { this.folder = folder; }

    public Boolean getHasAttachments() { return hasAttachments; }
    public void setHasAttachments(Boolean hasAttachments) { this.hasAttachments = hasAttachments; }
}