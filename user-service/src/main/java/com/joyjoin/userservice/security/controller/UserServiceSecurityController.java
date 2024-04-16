package com.joyjoin.userservice.security.controller;

import com.joyjoin.userservice.model.User;
import com.joyjoin.userservice.security.model.AuthenticationRequest;
import com.joyjoin.userservice.security.model.AuthenticationResponse;
import com.joyjoin.userservice.security.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class UserServiceSecurityController {

    private final AuthService authService;

    public UserServiceSecurityController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthenticationResponse registerUser(@Valid @RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public AuthenticationResponse loginUser(@RequestBody AuthenticationRequest request) {
        return authService.login(request);
    }
}
