package com.micro.cart.client;

import com.micro.cart.common.ApiResponse;
import com.micro.cart.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
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

    public ProductDto getProductById(Long productId, String token) {
        ApiResponse<ProductDto> response = webClientBuilder.build()
                .get()
                .uri(productServiceUrl + "/product/" + productId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(RuntimeException::new)
                )
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<ProductDto>>() {})
                .block();

        if (response != null && response.getData() != null) {
            return response.getData();
        }
        
        throw new RuntimeException("Product not found or invalid response");
    }
}
