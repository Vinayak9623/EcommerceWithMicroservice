package com.micro.order.client;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class UserClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.user-service.url}")
    private String userServiceUrl;

    public UserResponse getUserById(Long userId, String token) {
        return webClientBuilder.build()
                .get()
                .uri(userServiceUrl + "/user/" + userId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class)
                                .map(RuntimeException::new)
                )
                .bodyToMono(UserResponseWrapper.class)
                .map(wrapper -> wrapper.getData())
                .block();
    }

    public void validateUser(Long userId, String token) {

        webClientBuilder.build()
                .get()
                .uri(userServiceUrl + "/user/validate/" + userId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class)
                                .map(RuntimeException::new)
                )
                .bodyToMono(Void.class)
                .block();
    }

    // Helper classes to handle the ApiResponse wrap
    @Data
    private static class UserResponseWrapper {
        private int status;
        private String message;
        private UserResponse data;
    }

    @Data
    public static class UserResponse {
        private Long id;
        private String name;
        private String email;
    }
}
