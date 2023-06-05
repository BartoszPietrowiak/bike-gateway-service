package com.bike.gateway.config;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocalHostRouteConfig {

    @Bean
    public RouteLocator localHostRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route(r -> r.path("/api/v1/bike*", "/api/v1/bike/*", "/api/v1/bikeUpc/*")
                        .uri("lb://bike-management-service"))
                .route(r -> r.path("/api/v1/customers/**")
                        .uri("lb://bike-order-service"))
                .route(r -> r.path("/api/v1/bike/*/inventory")
                        .filters(f -> f.circuitBreaker(c -> c.setName("inventoryCB")
                                .setFallbackUri("forward:/bike-inventory-failover")
                                .setRouteId("bike-inv-failover")
                        ))
                        .uri("lb://bike-inventory-service"))
                .route(r -> r.path("/bike-inventory-failover/**")
                        .uri("lb://bike-inventory-failover"))
                .build();
    }

}