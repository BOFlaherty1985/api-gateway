package com.investment.apigateway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import server.companydetails.CompanyDetailsServerResponse;
import server.companydetails.CompanyDetailsServerResponseBuilder;

@Component
public class CompanyService {

    private WebClient.Builder webClientBuilder;

    @Autowired
    public CompanyService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<CompanyDetailsServerResponse> getCompanyResult(String ticker) {

        if (ticker == null || ticker.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Mono<CompanyDetailsServerResponse> result =
                webClientBuilder.build().get()
                        .uri("http://company-service/companyOverview?ticker=" + ticker)
                        .retrieve()
                        .bodyToMono(CompanyDetailsServerResponse.class);

        return result;
    }
}
