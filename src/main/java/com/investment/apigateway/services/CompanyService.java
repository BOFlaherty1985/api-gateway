package com.investment.apigateway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import server.companydetails.CompanyDetailsServerResponse;

import java.util.Optional;

@Component
public class CompanyService {

    private WebClient.Builder webClientBuilder;

    @Autowired
    public CompanyService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<CompanyDetailsServerResponse> getCompanyResult(Optional<String> ticker,
                                                               Optional<String> jwtToken) {

        if (ticker.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Mono<CompanyDetailsServerResponse> result =
                webClientBuilder.build().get()
                        .uri("http://company-service/companyOverview?ticker=" + ticker.get())
                        .header("Authorization", "Bearer " + jwtToken.get())
                        .retrieve()
                        .bodyToMono(CompanyDetailsServerResponse.class);

        return result;
    }
}
