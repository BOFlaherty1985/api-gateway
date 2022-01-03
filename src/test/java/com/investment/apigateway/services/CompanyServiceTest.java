package com.investment.apigateway.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import problemdetail.ProblemDetail;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import server.companydetails.CompanyDetailsServerResponse;
import server.companydetails.CompanyDetailsServerResponseBuilder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private WebClient webClientMock;
    @Mock
    private WebClient.Builder webClientBuilderMock;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
    @Mock
    WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.ResponseSpec responseMock;

    @BeforeEach
    public void setup() {
        companyService = new CompanyService(webClientBuilderMock);
    }

    // https://dzone.com/articles/spring-5-web-reactive-flux-and-mono
    @Test
    @Disabled // TODO - duplicated test?
    public void shouldReturnCompanyResultForGivenTicker() {
        // given
        when(webClientBuilderMock.build()).thenReturn(webClientMock);
        when(webClientBuilderMock.build().get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersMock.header(any(), any())).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);

        Optional<String> ticker = Optional.of("IBM");

        // when
        CompanyDetailsServerResponse response = CompanyDetailsServerResponseBuilder.builder.build();
        when(responseMock.bodyToMono(CompanyDetailsServerResponse.class)).thenReturn(Mono.just(response));

        Mono<CompanyDetailsServerResponse> result = companyService.getCompanyResult(ticker);

        // then
        StepVerifier.create(result.log())
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    public void shouldThrowExceptionWhenTickerIsEmpty() {
        // given
        Optional<String> ticker = Optional.empty();

        // when
        assertThrows(IllegalArgumentException.class, () -> companyService.getCompanyResult(ticker));
    }

    @Test
    public void shouldReturnMockResponseFromCompanyDetailsService() {
        // given
        when(webClientBuilderMock.build()).thenReturn(webClientMock);
        when(webClientBuilderMock.build().get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri("http://company-service/companyOverview?ticker=IBM")).thenReturn(requestHeadersMock);
//        when(requestHeadersUriMock.header("correlation-id")).thenReturn(requestHeadersMock);
//        when(requestHeadersUriMock.headers(notNull())).thenReturn(requestHeadersMock);
//        when(requestHeadersMock.header("correlation-id")).thenReturn(UUID.randomUUID().toString());
//        when(requestHeadersMock.header(any(), any())).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);

        Optional<String> ticker = Optional.of("IBM");
        Optional<String> name = Optional.of("International Business Machines");
        Optional<String> description = Optional.of("blah blah");
        Optional<String> sector = Optional.of("Technology");
        Optional<String> peRatio = Optional.of("20.0");
        Optional<String> exchange = Optional.of("NYSE");
        Optional<ProblemDetail> problemDetail = Optional.empty();
        CompanyDetailsServerResponse companyDetailsServerResponse = CompanyDetailsServerResponseBuilder.builder
                .symbol(ticker)
                .name(name)
                .description(description)
                .sector(sector)
                .peRatio(peRatio)
                .exchange(exchange)
                .problemDetail(problemDetail)
                .build();
        when(responseMock.bodyToMono(CompanyDetailsServerResponse.class)).thenReturn(Mono.just(companyDetailsServerResponse));

        // when
        Mono<CompanyDetailsServerResponse> result = companyService.getCompanyResult(ticker);

        // then
        StepVerifier.create(result.log())
                .expectNext(companyDetailsServerResponse)
                .verifyComplete();
    }
}
