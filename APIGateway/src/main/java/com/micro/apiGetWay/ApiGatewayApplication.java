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
                // Swagger Documentation Routes (Require RewritePath)
                .route("product-swagger", p -> p
                        .path("/product/v3/api-docs")
                        .filters(f -> f.rewritePath("/product/(?<segment>.*)", "/${segment}"))
                        .uri("lb://product-service"))
                .route("order-swagger", p -> p
                        .path("/order/v3/api-docs")
                        .filters(f -> f.rewritePath("/order/(?<segment>.*)", "/${segment}"))
                        .uri("lb://order-service"))
                .route("user-swagger", p -> p
                        .path("/user/v3/api-docs")
                        .filters(f -> f.rewritePath("/user/(?<segment>.*)", "/${segment}"))
                        .uri("lb://user-service"))
                .route("cart-swagger", p -> p
                        .path("/cart/v3/api-docs")
                        .filters(f -> f.rewritePath("/cart/(?<segment>.*)", "/${segment}"))
                        .uri("lb://cart-service"))
                .route("notification-swagger", p -> p
                        .path("/notification/v3/api-docs")
                        .filters(f -> f.rewritePath("/notification/(?<segment>.*)", "/${segment}"))
                        .uri("lb://notification-service"))
                
                // Existing Application Routes
                .route("product-service-route", p -> p
                        .path("/product/**")
                        .filters(f -> f.addRequestHeader("service", "product-service"))
                        .uri("lb://product-service"))
                .route("order-service-route", p -> p
                        .path("/order/**")
                        .filters(f -> f.addRequestHeader("service", "order-service"))
                        .uri("lb://order-service"))
                .route("user-service-route", p -> p
                        .path("/user/**")
                        .filters(f -> f.addRequestHeader("service", "user-service"))
                        .uri("lb://user-service"))
                .route("cart-service-route", p -> p
                        .path("/cart/**")
                        .filters(f -> f.addRequestHeader("service", "cart-service"))
                        .uri("lb://cart-service"))
                .route("notification-service-route", p -> p
                        .path("/notification/**")
                        .filters(f -> f.addRequestHeader("service", "notification-service"))
                        .uri("lb://notification-service"))
                .build();
    }



}