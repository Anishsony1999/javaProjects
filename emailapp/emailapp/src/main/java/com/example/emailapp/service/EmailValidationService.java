package com.example.emailapp.service;

import com.example.emailapp.dto.EmailRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Service
public class EmailValidationService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );

    public boolean validateEmailRequest(EmailRequest emailRequest) {
        if (emailRequest == null) {
            return false;
        }

        // Validate recipients
        if (emailRequest.getTo() == null || emailRequest.getTo().isEmpty()) {
            return false;
        }

        for (String email : emailRequest.getTo()) {
            if (!isValidEmail(email)) {
                return false;
            }
        }

        // Validate CC if provided
        if (emailRequest.getCc() != null) {
            for (String email : emailRequest.getCc()) {
                if (!isValidEmail(email)) {
                    return false;
                }
            }
        }

        // Validate BCC if provided
        if (emailRequest.getBcc() != null) {
            for (String email : emailRequest.getBcc()) {
                if (!isValidEmail(email)) {
                    return false;
                }
            }
        }

        // Validate subject and content
        return StringUtils.hasText(emailRequest.getSubject()) &&
                StringUtils.hasText(emailRequest.getContent());
    }

    public boolean isValidEmail(String email) {
        return StringUtils.hasText(email) && EMAIL_PATTERN.matcher(email).matches();
    }
}
