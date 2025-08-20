package com.example.emailapp.service;

import com.example.emailapp.config.EmailReadingProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.*;
import java.util.Properties;

@Service
public class EmailHealthService {

    private static final Logger logger = LoggerFactory.getLogger(EmailHealthService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailReadingProperties emailProperties;

    public boolean checkSMTPConnection() {
        try {
            // Test SMTP connection
            Session session = mailSender.createMimeMessage().getSession();
            Transport transport = session.getTransport("smtp");
            transport.connect();
            transport.close();
            logger.info("SMTP connection test successful");
            return true;
        } catch (Exception e) {
            logger.error("SMTP connection test failed", e);
            return false;
        }
    }

    public boolean checkIMAPConnection() {
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
            folder.close(false);
            store.close();

            logger.info("IMAP connection test successful");
            return true;
        } catch (Exception e) {
            logger.error("IMAP connection test failed", e);
            return false;
        }
    }

    public EmailHealthStatus getHealthStatus() {
        boolean smtpHealthy = checkSMTPConnection();
        boolean imapHealthy = checkIMAPConnection();

        return new EmailHealthStatus(smtpHealthy, imapHealthy);
    }

    public static class EmailHealthStatus {
        private boolean smtpHealthy;
        private boolean imapHealthy;
        private boolean overallHealthy;

        public EmailHealthStatus(boolean smtpHealthy, boolean imapHealthy) {
            this.smtpHealthy = smtpHealthy;
            this.imapHealthy = imapHealthy;
            this.overallHealthy = smtpHealthy && imapHealthy;
        }

        // Getters
        public boolean isSmtpHealthy() { return smtpHealthy; }
        public boolean isImapHealthy() { return imapHealthy; }
        public boolean isOverallHealthy() { return overallHealthy; }
    }
}