package com.micro.order.externalServices;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="PRODUCT-SERVICE")
public interface ProductClient {

    @GetMapping("/product/{productId}")
    ProductDto getProductById(@PathVariable("productId") Long productId, @RequestHeader("Authorization") String token);
}
