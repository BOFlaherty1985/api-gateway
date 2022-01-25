package com.investment.apigateway.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import server.authenticationserver.AuthenticationRequest;
import server.authenticationserver.AuthenticationRequestBuilder;
import server.authenticationserver.AuthenticationServerResponse;
import server.authenticationserver.AuthenticationServerResponseBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServerTest {

    @InjectMocks
    private AuthenticationServer authenticationServer;

    @Mock
    private RestTemplate restTemplate;

    private final String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImV4cCI6MTY0MzA3Mzk2OX0.mcp42Nl4tB2ApXjXAYhGDoS6lPLcbFzY_475dXw_2-A";

    @Test
    public void shouldRetrieveJwtTokenFromAuthenticationServer() {
        // given
        final String username = "testUser";
        final String password = "password";
        AuthenticationRequest request = AuthenticationRequestBuilder.authenticationRequestBuilder()
                .username(username)
                .password(password)
                .build();

        AuthenticationServerResponse authenticationServerResponse = AuthenticationServerResponseBuilder
                .authenticationServerResponseBuilder()
                .jwtToken(jwtToken)
                .build();

        when(restTemplate.getForEntity(String.format("http://localhost:2222/authenticate?username=%s&password=%s", username, password),
                AuthenticationServerResponse.class)).thenReturn(ResponseEntity.ok(authenticationServerResponse));

        // when
        AuthenticationServerResponse result = authenticationServer.authenticateUser(request);

        // then
        assertNotNull(request);
        assertEquals(jwtToken, result.getJwtToken());
    }

}
