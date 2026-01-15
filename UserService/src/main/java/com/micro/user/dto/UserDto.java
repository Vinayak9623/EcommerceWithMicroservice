package com.micro.user.dto;

import com.micro.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private String name;
    private String mobileNumber;
    private String email;
    private String password;
    private Role role;
}
