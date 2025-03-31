package com.micro.user.FeignClientServices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class OrderResonseDto {

    private Long id;
    private Long userId;
    private Long productId;
    private int quantity;
    private LocalDate orderDate;
}
