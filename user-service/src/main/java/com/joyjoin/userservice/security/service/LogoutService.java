package com.joyjoin.userservice.security.service;

import com.joyjoin.userservice.security.model.Token;
import com.joyjoin.userservice.security.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        String tokenPrefix = "Bearer ";
        if (authHeader == null || !authHeader.startsWith(tokenPrefix)) {
            return;
        }
        jwtToken = authHeader.substring(tokenPrefix.length());
        Optional<Token> token = tokenRepository.findByToken(jwtToken);
        token.ifPresent(tokenRepository::delete);
    }
}
