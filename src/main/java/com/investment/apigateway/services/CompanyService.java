package com.investment.apigateway.services;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CompanyService {
    public Mono<String> getCompanyResult(String ticker) {
        return Mono.just("testa");
    }
}
