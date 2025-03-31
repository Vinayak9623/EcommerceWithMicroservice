package com.micro.product.feignClientServices;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE",configuration = FeignConfig.class)
public interface UserServiceFeignClient {

    @GetMapping("/user/role")
    String getUserRole(@RequestHeader("Authorization") String token);


}

