package com.micro.product.jwtServices;

import com.micro.product.Costomexception.UnauthorizedException;
import com.micro.product.feignClientServices.UserServiceFeignClient;
import feign.FeignException;
import org.springframework.stereotype.Service;

@Service
public class JwtValidationService {

    private final UserServiceFeignClient userServiceFeignClient;

    public JwtValidationService(UserServiceFeignClient userServiceFeignClient) {
        this.userServiceFeignClient = userServiceFeignClient;
    }

    public void validateAdminRole(String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                throw new UnauthorizedException("Invalid token format");
            }

            String role = userServiceFeignClient.getUserRole(token);
            if (role == null || !role.equals("ADMIN")) {
                throw new UnauthorizedException("Only ADMIN users can perform this action");
            }
        } catch (FeignException e) {
            if (e.status() == 403) {
                throw new UnauthorizedException("Access denied. Invalid permissions.");
            }
            throw new UnauthorizedException("Token validation failed: " + e.getMessage());
        }
    }
}
