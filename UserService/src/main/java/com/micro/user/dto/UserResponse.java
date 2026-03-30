package com.micro.user.dto;

import com.micro.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserResponse implements Serializable {
    private Long id;
    private String name;
    private String mobileNumber;
    private String email;
    private Role role;
}
