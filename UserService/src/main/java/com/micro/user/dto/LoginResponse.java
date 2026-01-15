package com.micro.user.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private boolean success;


    public LoginResponse(String token, boolean success) {
        this.token = token;
        this.success = success;
    }
}
