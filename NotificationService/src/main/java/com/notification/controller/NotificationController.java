package com.notification.controller;

import com.notification.common.ApiResponse;
import com.notification.dto.EmailRequest;
import com.notification.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/send-order-confirmation")
    public ResponseEntity<ApiResponse<String>> sendOrderConfirmation(@RequestBody EmailRequest emailRequest) throws MessagingException {
        emailService.sendOrderConfirmationEmail(emailRequest);
        ApiResponse<String> response = new ApiResponse<>(
                200,
                "Email sent successfully",
                "Sent to: " + emailRequest.getToEmail(),
                null,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }
}
