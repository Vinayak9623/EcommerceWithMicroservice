package com.micro.user.dto;

import com.micro.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String mobileNumber;
    private String email;
    private Role role;
}
