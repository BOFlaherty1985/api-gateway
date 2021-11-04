package com.investment.apigateway.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(MockitoJUnitRunner.class)
public class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    // https://dzone.com/articles/spring-5-web-reactive-flux-and-mono
    @Test
    public void shouldReturnCompanyResultForGivenTicker() {
        // given
        String ticker = "IBM";
        
        // when
        Mono<String> result = companyService.getCompanyResult(ticker);

        // then
        StepVerifier.create(result.log())
                .expectNext("test")
                .verifyComplete();
    }

}
