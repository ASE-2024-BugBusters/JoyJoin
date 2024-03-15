package com.joyjoin.securityservice.controller;

import com.joyjoin.securityservice.model.UserCredential;
import com.joyjoin.securityservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public UserCredential addNewUser(@RequestBody UserCredential userCredential) {
        return service.saveUser(userCredential);
    }

    @GetMapping("/token")
    public String getToken(UserCredential userCredential) {
        return service.generateToken(userCredential.getName());
    }

    @GetMapping("/validate")
    public String getToken(@RequestParam("token") String token) {
        return service.validateToken(token);
    }


}
