package com.khotixs.portal_gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TokenRelayFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token != null) {
            exchange.getRequest().mutate()
                .header("Authorization", token)
                .build();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // Ensures the filter runs early
    }
}
