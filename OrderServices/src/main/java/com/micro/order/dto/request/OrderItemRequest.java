package com.micro.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class OrderItemRequest {
    private Long productId;
    private int quantity;
    private double price;
}
