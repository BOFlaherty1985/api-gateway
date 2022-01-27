package com.investment.apigateway.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import server.technicalanalysis.Indicator;
import server.technicalanalysis.TechnicalAnalysisServerResponse;
import server.technicalanalysis.TechnicalAnalysisServerResponseBuilder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TechnicalAnalysisServiceTest extends WebClientTest {

    private TechnicalAnalysisService technicalAnalysisService;

    // TODO - Mockito / Integration Test Approach blog post - what approach have I taken? Why? What were the issues

    @BeforeEach
    public void setup() {
        technicalAnalysisService = new TechnicalAnalysisService(webClientBuilderMock);
    }

    // May not work due to the use of @LoadBalanced.
    // Change the code (TechnicalAnalysisService) to hit an actual endpoint /test - What happens? - it works Ok. MockWebServer does not seem to like the Eureka URL
    @Test
    public void shouldFailValidationWhenTickerIsEmptyOrNull() {
        // given
        Optional<String> ticker = Optional.empty();
        Optional<String> stockPrice = Optional.of("200.00");

        // when
        Throwable exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            technicalAnalysisService.getSimpleMovingDayAverageResult(ticker, stockPrice, jwtToken);
        });

        assertEquals(exception.getMessage(), "Ticker & StockPrice must not be empty or null.");
    }

    @Test
    public void shouldFailValidationWhenStockPriceIsEmptyOrNull() {
        // given
        Optional<String> stockPrice = Optional.empty();

        // when
        Throwable exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Optional<String> ticker = Optional.of("IBM");
            technicalAnalysisService.getSimpleMovingDayAverageResult(ticker, stockPrice, jwtToken);
        });

        assertEquals(exception.getMessage(), "Ticker & StockPrice must not be empty or null.");
    }

    @Test
    public void shouldReturnMockedResponseFromTechnicalAnalysisService() {
        // given
        setupWebClient("http://technical-analysis-service/simpleMovingDayAnalysis?ticker=IBM&stockPrice=200.0", true);

        TechnicalAnalysisServerResponse technicalAnalysisServiceResponse = TechnicalAnalysisServerResponseBuilder.builder()
                .indicator(Optional.of(Indicator.BEARISH))
                .build();
        when(responseMock.bodyToMono(TechnicalAnalysisServerResponse.class)).thenReturn(Mono.just(technicalAnalysisServiceResponse));

        Optional<String> ticker = Optional.of("IBM");
        Optional<String> stockPrice = Optional.of("200.0");

        // when
        Mono<TechnicalAnalysisServerResponse> result = technicalAnalysisService.getSimpleMovingDayAverageResult(ticker, stockPrice, jwtToken);

        // then
        StepVerifier.create(result.log())
                .expectNext(technicalAnalysisServiceResponse)
                .verifyComplete();
    }
}
