package com.bappy.application.controller;

import com.bappy.application.dto.QueryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/query")
@RequiredArgsConstructor
public class QueryController {

    private final WebClient.Builder webClientBuilder;

    @Value("${llm.python-proxy-url}")
    private String pythonProxyUrl;

    @PostMapping
    public Mono<ResponseEntity<String>> query(@RequestBody QueryRequest body, @AuthenticationPrincipal Jwt jwt) {
        WebClient client = webClientBuilder.build();
        return client.post()
                .uri(pythonProxyUrl + "/query")
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .toEntity(String.class);
    }
}
