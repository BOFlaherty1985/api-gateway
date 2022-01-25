package com.investment.apigateway.services;

import org.junit.Before;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebClientTest {

    @Mock
    protected WebClient webClientMock;
    @Mock
    protected WebClient.Builder webClientBuilderMock;
    @Mock
    protected WebClient.RequestHeadersSpec requestHeadersMock;
    @Mock
    protected WebClient.RequestHeadersUriSpec requestHeadersUriMock;
    @Mock
    protected WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    protected WebClient.ResponseSpec responseMock;

    protected void setupWebClient(String uri, boolean includesHttpHeader) {
        when(webClientBuilderMock.build()).thenReturn(webClientMock);
        when(webClientBuilderMock.build().get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(uri)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        if (includesHttpHeader) {
            when(requestHeadersMock.header(any(),any())).thenReturn(requestHeadersMock);
        }
    }
}
