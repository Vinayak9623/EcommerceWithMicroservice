package com.micro.order.externalServices;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "user-service",
        url = "${user.service.url:http://localhost:8090}",
        path = "/user/"
)
public interface UserClient {

    @GetMapping("/user/{userId}")
    UserDto getUserById(@PathVariable("userId") Long userId);


    @GetMapping("/userDetails")
    UserDto getUserDetails(@RequestHeader ("Authorization") String token);

      }
