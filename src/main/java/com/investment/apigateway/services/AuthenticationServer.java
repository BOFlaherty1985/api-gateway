package com.investment.apigateway.services;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import server.authenticationserver.AuthenticationRequest;
import server.authenticationserver.AuthenticationServerResponse;
import server.authenticationserver.AuthenticationServerResponseBuilder;

@Component
public class AuthenticationServer {

    public Mono<AuthenticationServerResponse> authenticateUser(AuthenticationRequest authenticationRequest) {
        AuthenticationServerResponse response = AuthenticationServerResponseBuilder.authenticationServerResponseBuilder()
                .jwtToken("jwt")
                .build();
        System.out.println("JWT Token");
        return Mono.just(response);
    }
}
