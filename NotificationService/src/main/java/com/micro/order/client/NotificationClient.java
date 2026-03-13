package com.micro.order.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class NotificationClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.notification-service.url}")
    private String notificationServiceUrl;

    public void sendOrderConfirmation(EmailRequest emailRequest, String token) {
        webClientBuilder.build()
                .post()
                .uri(notificationServiceUrl + "/api/notifications/send-order-confirmation")
                .header(HttpHeaders.AUTHORIZATION, token)
                .bodyValue(emailRequest)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class)
                                .map(RuntimeException::new)
                )
                .bodyToMono(String.class)
                .block();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EmailRequest {
        private String toEmail;
        private String subject;
        private String customerName;
        private Long orderId;
        private BigDecimal totalAmount;
        private String orderStatus;
    }
}
