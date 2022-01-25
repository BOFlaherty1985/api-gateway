package com.investment.apigateway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import server.authenticationserver.AuthenticationRequest;
import server.authenticationserver.AuthenticationServerResponse;

@Component
public class AuthenticationServer {

    private WebClient.Builder webClientBuilder;
    private RestTemplate restTemplate;

    @Autowired
    public AuthenticationServer(WebClient.Builder webClientBuilder, RestTemplate restTemplate) {
        this.webClientBuilder = webClientBuilder;
        this.restTemplate = restTemplate;
    }

//    public Mono<AuthenticationServerResponse> authenticateUser(AuthenticationRequest authenticationRequest) {
//        return webClientBuilder.build()
//                .get()
//                .uri(String.format("http://authentication-server/authenticate?username=%s&password=%s",
//                        authenticationRequest.getUsername(), authenticationRequest.getPassword()))
//                .retrieve()
//                .bodyToMono(AuthenticationServerResponse.class);
//    }

    public AuthenticationServerResponse authenticateUser(AuthenticationRequest authenticationRequest) {
        ResponseEntity<AuthenticationServerResponse> responseEntity = restTemplate.getForEntity(String.format("http://localhost:2222/authenticate?username=%s&password=%s",
                authenticationRequest.getUsername(), authenticationRequest.getPassword()), AuthenticationServerResponse.class);
        return responseEntity.getBody();
    }
}
