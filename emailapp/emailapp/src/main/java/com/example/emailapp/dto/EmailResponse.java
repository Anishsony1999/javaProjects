package com.example.emailapp.dto;

import java.time.LocalDateTime;

public class EmailResponse {
    private Long id;
    private String messageId;
    private String sender;
    private String recipients;
    private String subject;
    private String content;
    private LocalDateTime sentDate;
    private LocalDateTime receivedDate;
    private Boolean isRead;
    private String folder;
    private Boolean hasAttachments;

    // Constructors
    public EmailResponse() {}

    public EmailResponse(Long id, String messageId, String sender, String recipients,
                         String subject, String content, LocalDateTime sentDate,
                         LocalDateTime receivedDate, Boolean isRead, String folder,
                         Boolean hasAttachments) {
        this.id = id;
        this.messageId = messageId;
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.content = content;
        this.sentDate = sentDate;
        this.receivedDate = receivedDate;
        this.isRead = isRead;
        this.folder = folder;
        this.hasAttachments = hasAttachments;
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