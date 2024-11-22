package com.khotixs.identity_service.feature.email;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
