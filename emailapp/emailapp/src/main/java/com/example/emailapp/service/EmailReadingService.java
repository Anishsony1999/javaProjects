package com.example.emailapp.service;

import com.example.emailapp.config.EmailReadingProperties;
import com.example.emailapp.model.Email;
import com.example.emailapp.repository.EmailRepository;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class EmailReadingService {

    private static final Logger logger = LoggerFactory.getLogger(EmailReadingService.class);

    @Autowired
    private EmailReadingProperties emailProperties;

    @Autowired
    private EmailRepository emailRepository;

    @Scheduled(fixedDelayString = "${email.reading.check-interval}")
    public void readEmails() {
        logger.info("Starting email reading process...");

        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", emailProperties.getProtocol());
            props.put("mail." + emailProperties.getProtocol() + ".host", emailProperties.getHost());
            props.put("mail." + emailProperties.getProtocol() + ".port", emailProperties.getPort());
            props.put("mail." + emailProperties.getProtocol() + ".ssl.enable", "true");

            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore(emailProperties.getProtocol());
            store.connect(emailProperties.getHost(), emailProperties.getUsername(), emailProperties.getPassword());

            Folder folder = store.getFolder(emailProperties.getFolder());
            folder.open(Folder.READ_ONLY);

            Message[] messages = folder.getMessages();
            logger.info("Found {} messages in folder {}", messages.length, emailProperties.getFolder());

            // Process only new messages (last 50 to avoid overwhelming the system)
            int startIndex = Math.max(0, messages.length - 50);
            for (int i = startIndex; i < messages.length; i++) {
                processMessage(messages[i]);
            }

            folder.close(false);
            store.close();

            logger.info("Email reading process completed successfully");

        } catch (Exception e) {
            logger.error("Error reading emails", e);
        }
    }

    private void processMessage(Message message) {
        try {
            String messageId = getMessageId(message);

            // Check if email already exists
            if (emailRepository.findByMessageId(messageId).isPresent()) {
                return; // Skip if already processed
            }

            String sender = message.getFrom() != null && message.getFrom().length > 0
                    ? message.getFrom()[0].toString() : "Unknown";

            String recipients = message.getAllRecipients() != null
                    ? Arrays.stream(message.getAllRecipients())
                    .map(Address::toString)
                    .collect(Collectors.joining(", "))
                    : "";

            String subject = message.getSubject() != null ? message.getSubject() : "No Subject";
            String content = getTextContent(message);

            LocalDateTime sentDate = message.getSentDate() != null
                    ? LocalDateTime.ofInstant(message.getSentDate().toInstant(), ZoneId.systemDefault())
                    : LocalDateTime.now();

            Email email = new Email(messageId, sender, recipients, subject, content, sentDate);
            email.setFolder(emailProperties.getFolder());
            email.setIsRead(message.isSet(Flags.Flag.SEEN));
            email.setHasAttachments(hasAttachments(message));

            emailRepository.save(email);
            logger.debug("Processed email: {} from {}", subject, sender);

        } catch (Exception e) {
            logger.error("Error processing message", e);
        }
    }

    private String getMessageId(Message message) throws MessagingException {
        String[] messageIds = message.getHeader("Message-ID");
        return messageIds != null && messageIds.length > 0
                ? messageIds[0] : "generated-" + System.currentTimeMillis();
    }

    private String getTextContent(Message message) throws MessagingException, IOException {
        if (message.isMimeType("text/plain")) {
            return (String) message.getContent();
        } else if (message.isMimeType("text/html")) {
            return (String) message.getContent();
        } else if (message.isMimeType("multipart/*")) {
            return getTextFromMimeMultipart((MimeMultipart) message.getContent());
        } else {
            return "Unsupported content type: " + message.getContentType();
        }
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart)
            throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();

        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);

            if (bodyPart.isMimeType("text/plain")) {
                result.append("\n").append(bodyPart.getContent());
            } else if (bodyPart.isMimeType("text/html")) {
                result.append("\n").append(bodyPart.getContent());
            } else if (bodyPart.isMimeType("multipart/*")) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }

        return result.toString();
    }

    private boolean hasAttachments(Message message) throws MessagingException, IOException {
        if (message.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) message.getContent();
            int count = multipart.getCount();

            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disposition = bodyPart.getDisposition();

                if (Part.ATTACHMENT.equalsIgnoreCase(disposition) ||
                        Part.INLINE.equalsIgnoreCase(disposition)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void manualEmailCheck() {
        logger.info("Manual email check triggered");
        readEmails();
    }
}