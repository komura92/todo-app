package com.example.todoapp.infrastructure.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class KeycloakClient {

    private final String keycloakHost;

    private static final Map<TestUser, TokenResponse> userCache = new HashMap<>();

    public KeycloakClient(Environment env) {
        keycloakHost = env.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri");
    }


    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class TokenResponse {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("session_state")
        private String sessionState;

        @JsonProperty("scope")
        private String scope;

        @JsonProperty("expires_in")
        private int expiresIn;

        @JsonProperty("refresh_expires_in")
        private int refreshExpiresIn;
    }


    public String getAccessToken(TestUser user) {
        if (userCache.containsKey(user)) {
            return userCache.get(user).accessToken;
        }
        return obtainNewToken(user);
    }

    private String obtainNewToken(TestUser user) {
        TokenResponse tokenResponse = getTokenResponse(user.getUsername(), user.getPassword());
        userCache.put(user, tokenResponse);
        return tokenResponse.accessToken;
    }

    private TokenResponse getTokenResponse(String user, String password) {
        return WebClient.builder()
                .build()
                .post()
                .uri(URI.create(keycloakHost + "/protocol/openid-connect/token"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(getLoginBody(user, password))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }

    private BodyInserters.FormInserter<String> getLoginBody(String user, String password) {
        return BodyInserters.fromFormData("username", user)
                .with("password", password)
                .with("grant_type", "password")
                .with("client_id", "custom-client");
    }
}