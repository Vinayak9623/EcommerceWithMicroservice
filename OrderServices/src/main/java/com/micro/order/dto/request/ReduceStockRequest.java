package com.micro.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ReduceStockRequest {
    private Long productId;
    private int quantity;
}
