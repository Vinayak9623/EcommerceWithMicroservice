package com.micro.order.client;

import com.micro.order.dto.request.ReduceStockRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ProductClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.product-service.url}")
    private String productServiceUrl;

//    public void reduceStock(Long productId, int quantity, String token) {
//
//        ReduceStockRequest request =
//                new ReduceStockRequest(productId, quantity);
//
//        webClientBuilder.build()
//                .put()
//                .uri(productServiceUrl + "/product/reduce-stock")
//                .header(HttpHeaders.AUTHORIZATION, token)
//                .bodyValue(request)
//                .retrieve()
//                .onStatus(
//                        HttpStatusCode::isError,
//                        response -> response.bodyToMono(String.class)
//                                .map(RuntimeException::new)
//                )
//                .bodyToMono(Void.class)
//                .block();
//    }

    public void reduceStock(Long productId, int quantity, String token) {
        callProductService(
                "/product/reduce-stock",
                new ReduceStockRequest(productId, quantity),
                token
        );
    }

    public void restoreStock(Long productId, int quantity, String token) {
        callProductService(
                "/product/restore-stock",
                new ReduceStockRequest(productId, quantity),
                token
        );
    }

    private void callProductService(
            String endpoint,
            ReduceStockRequest request,
            String token) {

        webClientBuilder.build()
                .put()
                .uri(productServiceUrl + endpoint)
                .header(HttpHeaders.AUTHORIZATION, token)
                .bodyValue(request)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class)
                                .map(RuntimeException::new)
                )
                .bodyToMono(Void.class)
                .block();
    }
}

