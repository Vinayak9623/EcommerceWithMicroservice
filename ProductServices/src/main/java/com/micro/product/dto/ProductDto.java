package com.micro.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto implements Serializable {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private String category;
    private String imageUrl;
}
