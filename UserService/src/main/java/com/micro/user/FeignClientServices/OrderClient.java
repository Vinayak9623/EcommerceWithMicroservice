package com.micro.user.FeignClientServices;

import com.micro.user.FeignClientServices.dto.OrderResonseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name ="ORDER-SERVICE")
public interface OrderClient {

    @GetMapping("order/user/{userId}")
    List<OrderResonseDto> getOrderByUserId(@PathVariable("userId") Long userId);
}
