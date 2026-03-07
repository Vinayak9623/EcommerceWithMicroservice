package com.micro.cart.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    private Long id;
    private Long userId;
    private double totalAmount;
    private List<CartItemDto> items = new ArrayList<>();
}
