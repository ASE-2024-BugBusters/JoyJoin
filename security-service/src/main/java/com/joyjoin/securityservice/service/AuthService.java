package com.joyjoin.securityservice.service;

import com.joyjoin.securityservice.model.UserCredential;
import com.joyjoin.securityservice.repository.UserCredentialRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserCredentialRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService authService;

    public AuthService(UserCredentialRepository repository, PasswordEncoder passwordEncoder, JwtService authService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    public UserCredential saveUser(UserCredential userCredential) {
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        return repository.save(userCredential);
    }

    public String generateToken(String username) {
        return authService.generateToken(username);
    }

    public String validateToken(String token) {
        System.out.println(authService.validateToken(token));
        return token;
    }
}
