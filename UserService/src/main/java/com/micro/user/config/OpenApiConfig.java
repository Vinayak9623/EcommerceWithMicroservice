package com.micro.user.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "User Service API",
                version = "1.0",
                description = "API documentation"
        ),
        servers = {
                @Server(url = "/", description = "Dynamic Server URL (Gateway/Direct)")
        }
)
public class OpenApiConfig {
}
