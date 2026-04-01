package com.micro.order.batch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderReportDTO {
    private Long orderId;
    private Long userId;
    private double totalAmount;
    private String status;
    private LocalDateTime orderDate;
}
