package com.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequest {
    private String toEmail;
    private String subject;
    private String customerName;
    private Long orderId;
    private BigDecimal totalAmount;
    private String orderStatus;
}
