package com.micro.apiGetWay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);

        System.out.println("Welcome to Api gateway");
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/product/**")
                        .filters(f -> f.addRequestHeader("service", "product-service"))
                        .uri("http://localhost:8091")).route(p -> p
                        .path("/order/**")
                        .filters(f -> f.addRequestHeader("service", "order-service"))
                        .uri("http://localhost:8092")).route(p -> p
                        .path("/user/**")
                        .filters(f -> f.addRequestHeader("service", "user-service"))
                        .uri("http://localhost:8090"))
                .build();
    }



}
