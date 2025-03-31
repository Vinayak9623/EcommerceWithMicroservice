package com.micro.order.dto;
import com.micro.order.externalServices.ProductDto;
import com.micro.order.externalServices.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class OrderDetailsDto {
    private Long orderId;
    private UserDto user;
    private ProductDto product;
    private int quantity;
    private LocalDate orderDate;
}
