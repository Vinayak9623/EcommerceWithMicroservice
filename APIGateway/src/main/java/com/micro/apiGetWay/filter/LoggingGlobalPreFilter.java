package com.micro.apiGetWay.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingGlobalPreFilter implements GlobalFilter {

    private final Logger logger = LoggerFactory.getLogger(LoggingGlobalPreFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().toString();
        
        // --- PUT YOUR BREAKPOINT ON THE LINE BELOW ---
        logger.info("API Gateway intercepted request to path: {}", requestPath);
        
        return chain.filter(exchange);
    }
}
