package com.investment.apigateway.dashboard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class StockDashboardConfiguration {

    @Bean
    public RouterFunction<ServerResponse> stockDashboardRouting(StockDashboardHandlers stockDashboardHandlers) {
        System.out.println("/stock/dashboard endpoint hit");
        return RouterFunctions.route()
                .GET("/stock/dashboard",
                        RequestPredicates.contentType(MediaType.APPLICATION_JSON).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        stockDashboardHandlers::stockDashboardHandler).build();
    }
}
