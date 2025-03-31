package com.micro.user.FeignClientServices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
public class OrderWithProductResponseDto {

    private Long orderId;
    private ProductResponseDto product;
    private int quantity;
    private LocalDate orderDate;
}
