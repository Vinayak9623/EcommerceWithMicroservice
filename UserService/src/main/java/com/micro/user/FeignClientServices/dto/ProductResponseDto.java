package com.micro.user.FeignClientServices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductResponseDto {

    private Long id;
    private String name;
    private String description;
    private double price;
   // private int stockQuantity;
    private String category;
}
