package com.investment.apigateway.integrationtest;

import com.investment.apigateway.services.TechnicalAnalysisService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import server.technicalanalysis.TechnicalAnalysisServerResponse;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
@Ignore
public class TechnicalAnalysisServiceIntegrationTest {

    private TechnicalAnalysisService technicalAnalysisService;
    private static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    public void beforeEach() {
        technicalAnalysisService = new TechnicalAnalysisService("localhost");
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

//    @Test
//    @Ignore
//    // May not work due to the use of @LoadBalanced.
//    // Change the code (TechnicalAnalysisService) to hit an actual endpoint /test - What happens? - it works Ok. MockWebServer does not seem to like the Eureka URL
//    public void test() {
//        // given
//        String ticker = "IBM";
//        String stockPrice = "200.00";
//
//        mockWebServer.enqueue(new MockResponse().setBody("testResponsea"));
//
//        // when
//        Mono<TechnicalAnalysisServerResponse> result = technicalAnalysisService.getSimpleMovingDayAverageResult(ticker, stockPrice);
//
//        // then
//        StepVerifier.create(result.log())
//                .expectNext(new TechnicalAnalysisServerResponse())
//                .verifyComplete();
//    }
}
