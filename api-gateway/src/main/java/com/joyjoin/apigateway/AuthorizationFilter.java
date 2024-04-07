package com.joyjoin.apigateway;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.List;
import java.util.Objects;

@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {
    @Autowired
    Environment env;
    private final String TOKEN_PREFIX = "Bearer ";

    public AuthorizationFilter() {
        super(Config.class);
    }

    public static class Config {}

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // Use the RequestPath to match different paths if they require certain roles
            String requestPath = exchange.getRequest().getPath().toString();
            if (!request.getHeaders().containsKey("Authorization")) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }
            String authHeader = Objects.requireNonNull(request.getHeaders().get("Authorization")).get(0);
            String jwt = authHeader.substring(TOKEN_PREFIX.length());
            if (!jwtIsValid(jwt)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        };
    }

    /**
     *
     * @param exchange
     * @param error
     * @param httpStatus
     * @return Error message if an error occured
     */
    private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    /**
     *
     * @param jwt JSON Web Token which needs to be validated
     * @return Boolean if the token is valid
     */
    private boolean jwtIsValid(String jwt) {
        boolean isValid = true;
        String subject = null;
        List roles = null;
        String tokenSecret = env.getProperty("jwt.token.secret");
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        Key signKey = Keys.hmacShaKeyFor(keyBytes);
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(jwt).getBody();
            subject = claims.getSubject();

            // use roles to check which permissions are allowed
            roles = claims.get("roles", List.class);
        } catch (Exception ex) {
            isValid = false;
        }
        if (subject == null || subject.isEmpty()) {
            isValid = false;
        }
        return isValid;
    }
}