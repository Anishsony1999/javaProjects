package com.example.emailapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class EmailRequest {
    @NotEmpty(message = "Recipients cannot be empty")
    private List<@Email(message = "Invalid email format") String> to;

    private List<@Email(message = "Invalid email format") String> cc;

    private List<@Email(message = "Invalid email format") String> bcc;

    @NotBlank(message = "Subject cannot be blank")
    private String subject;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    private boolean isHtml = false;

    private List<String> attachmentPaths;

    // Constructors
    public EmailRequest() {}

    public EmailRequest(List<String> to, String subject, String content) {
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    // Getters and Setters
    public List<String> getTo() { return to; }
    public void setTo(List<String> to) { this.to = to; }

    public List<String> getCc() { return cc; }
    public void setCc(List<String> cc) { this.cc = cc; }

    public List<String> getBcc() { return bcc; }
    public void setBcc(List<String> bcc) { this.bcc = bcc; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public boolean isHtml() { return isHtml; }
    public void setHtml(boolean html) { isHtml = html; }

    public List<String> getAttachmentPaths() { return attachmentPaths; }
    public void setAttachmentPaths(List<String> attachmentPaths) { this.attachmentPaths = attachmentPaths; }
}