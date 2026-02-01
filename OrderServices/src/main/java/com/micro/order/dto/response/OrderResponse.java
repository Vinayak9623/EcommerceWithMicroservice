package com.micro.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderResponse {
    private Long id;
    private Long userId;
    private Long productId;
    private int quantity;
    private double price;
    private LocalDateTime orderDate;
    private String status;
}
