package com.micro.user.FeignClientServices;

import com.micro.user.FeignClientServices.dto.OrderResonseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;


@FeignClient(
        name = "order-service",
        url = "${order.service.url:http://localhost:8092}",
        path = "/order/"
)
public interface OrderClient {

//    @GetMapping("order/user/{userId}")
//    List<OrderResonseDto> getOrderByUserId(@PathVariable("userId") Long userId);


    @GetMapping("user/{userId}")
    List<OrderResonseDto> getOrderByUserId(
            @PathVariable("userId") Long userId,
            @RequestHeader("Authorization") String token
    );


}
