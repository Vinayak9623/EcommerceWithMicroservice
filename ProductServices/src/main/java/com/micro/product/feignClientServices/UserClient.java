package com.micro.product.feignClientServices;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "user-service",
        url = "${user.service.url:http://localhost:8090}",
        path = "/user/"
)
public interface UserClient {

    @GetMapping("/userDetails")
    ResponseEntity<UserDto> getUserDetails(@RequestHeader("Authorization") String token);
}


