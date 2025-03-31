package com.micro.order.externalServices;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="USER-SERVICE",configuration = FeignClientConfig.class)
public interface UserClient {

    @GetMapping("/user/{userId}")
    UserDto getUserById(@PathVariable("userId") Long userId);

      }
