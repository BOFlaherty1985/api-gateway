package com.investment.apigateway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import server.technicalanalysis.TechnicalAnalysisServerResponse;

import java.util.Optional;

import static java.util.UUID.randomUUID;

@Service
public class TechnicalAnalysisService {

    // https://spring.io/guides/gs/spring-cloud-loadbalancer/
    // https://spring.io/blog/2020/03/25/spring-tips-spring-cloud-loadbalancer // TODO - BLOG POST MATERIAL
    private WebClient.Builder webClientBuilder;
    private WebClient webClient;

    // https://www.callicoder.com/spring-5-reactive-webclient-webtestclient-examples/
    // https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html

    @Autowired
    public TechnicalAnalysisService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    // TODO - used for Integration Test, but can be removed if I cannot get IT's to run with Eureka / LoadBalanced endpoint - LINK TO BLOG POST MATERIAL ON LOAD BALANCING
    public TechnicalAnalysisService(String baseUrl) {
        webClientBuilder = WebClient.builder().baseUrl(baseUrl);
    }

    public Mono<TechnicalAnalysisServerResponse> getSimpleMovingDayAverageResult(Optional<String> ticker,
                                                                                 Optional<String> stockPrice) {

        if (ticker.isEmpty()|| stockPrice.isEmpty()) {
            throw new IllegalArgumentException("Ticker & StockPrice must not be empty or null.");
        }

        // TODO - how do we handle HTTP status errors? Do we include errors within the JSON response or create here when the request fails - BLOG POST MATERIAL
        // https://www.baeldung.com/rest-api-error-handling-best-practices
        // https://medium.com/@sunitparekh/guidelines-on-json-responses-for-restful-services-1ba7c0c015d
        // https://dzone.com/articles/top-rest-api-best-practices
        // https://dzone.com/articles/error-handling-in-spring-webflux

        // Lets use - RFC-7807 - Problem Details specification (https://datatracker.ietf.org/doc/html/rfc7807) - 3.1 Members of Problem Detail Object
        // https://blog.codecentric.de/en/2020/01/rfc-7807-problem-details-with-spring-boot-and-jax-rs/

        // https://reflectoring.io/spring-webclient/ - onStatus(...)
        // https://lakitna.medium.com/understanding-problem-json-adf68e5cf1f8

        // https://github.com/spring-projects/spring-boot/issues/19525

        /// Can we return a Response entity, but .flatMap( to TechnicalAnalysisResponse class?)
        String correlation_id = randomUUID().toString();
        System.out.print("API Gateway Correlation Id: " + correlation_id);
        Mono<TechnicalAnalysisServerResponse> responseMono = webClientBuilder.build()
                .get()
                .uri("http://technical-analysis-service/simpleMovingDayAnalysis?ticker="
                        + ticker.get() + "&stockPrice=" + stockPrice.get()) // http://{service-name}/endpoint
                .header("correlation-id", correlation_id)
                .retrieve()
                .bodyToMono(TechnicalAnalysisServerResponse.class);

        return responseMono;
    }
}