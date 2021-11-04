package com.investment.apigateway.dashboard;

import com.investment.apigateway.services.CompanyService;
import com.investment.apigateway.services.TechnicalAnalysisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import reactor.core.publisher.Mono;
import server.technicalanalysis.Indicator;
import server.technicalanalysis.TechnicalAnalysisServerResponse;
import server.technicalanalysis.TechnicalAnalysisServerResponseBuilder;

import java.net.URI;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StockDashboardHandlersTest {

    @InjectMocks
    private StockDashboardHandlers stockDashboardHandlers;

    @Mock
    private TechnicalAnalysisService technicalAnalysisService;

    @Mock
    private CompanyService companyService;

    @Test
    public void shouldCallTechnicalAnalysisService() {
        // given
        MockServerRequest serverRequest = MockServerRequest.builder()
                .uri(URI.create("http://test.com")).build();

        String ticker = "IBM";
        String stockPrice = "200.00";

        TechnicalAnalysisServerResponse response = TechnicalAnalysisServerResponseBuilder
                .builder()
                .indicator(Optional.of(Indicator.BEARISH))
                .build();

        given(technicalAnalysisService.getSimpleMovingDayAverageResult(ticker, stockPrice))
                .willReturn(Mono.just(response));

        // when
        stockDashboardHandlers.stockDashboardHandler(serverRequest);

        // then
        verify(technicalAnalysisService).getSimpleMovingDayAverageResult(ticker, stockPrice);
    }

    @Test
    public void shouldCallCompanyService() {
        // given
        MockServerRequest serverRequest = MockServerRequest.builder()
                .uri(URI.create("http://test.com")).build();

        String ticker = "IBM";

        // when
        stockDashboardHandlers.stockDashboardHandler(serverRequest);

        // then
        verify(companyService).getCompanyResult(ticker);
    }

}
