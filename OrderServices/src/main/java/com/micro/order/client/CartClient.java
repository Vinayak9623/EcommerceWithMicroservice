package com.micro.order.client;

import com.micro.order.common.ApiResponse;
import com.micro.order.dto.response.CartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class CartClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.cart-service.url}")
    private String cartServiceUrl;

    public CartDto getCart(Long userId, String token) {
        ApiResponse<CartDto> response = webClientBuilder.build()
                .get()
                .uri(cartServiceUrl + "/cart/" + userId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        errorResponse -> errorResponse.bodyToMono(String.class)
                                .map(RuntimeException::new)
                )
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<CartDto>>() {})
                .block();

        if (response != null && response.getData() != null) {
            return response.getData();
        }
        return null;
    }

    public void clearCart(Long userId, String token) {
        webClientBuilder.build()
                .delete()
                .uri(cartServiceUrl + "/cart/clear/" + userId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        errorResponse -> errorResponse.bodyToMono(String.class)
                                .map(RuntimeException::new)
                )
                .bodyToMono(Void.class)
                .block();
    }
}
