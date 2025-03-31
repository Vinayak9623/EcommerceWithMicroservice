package com.micro.user.FeignClientServices;

import com.micro.user.FeignClientServices.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

    @GetMapping("/product/{productId}")
    ProductResponseDto getProductById(@PathVariable Long productId);
}
