package com.notification.service;

import com.notification.dto.EmailRequest;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendOrderConfirmationEmail(EmailRequest emailRequest) throws MessagingException;
}
