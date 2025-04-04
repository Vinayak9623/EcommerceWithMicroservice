package com.micro.product.feignClientServices;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
private int id;
private String name;
private String email;
private String password;
@Enumerated(EnumType.STRING)
private Role role;
}
