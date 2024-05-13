package com.joyjoin.userservice.security.controller;

import com.joyjoin.userservice.exception.EmailAlreadyExistsException;
import com.joyjoin.userservice.exception.ResourceNotFoundException;
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

    /**
     * registerUser let new user for our platform create his account to use our service.
     *
     * @param user information of this user, including email, password, accountName, etc.
     * @return user ID and his JWT token
     * @throws EmailAlreadyExistsException if existing email is used here
     */
    @PostMapping("/register")
    public AuthenticationResponse registerUser(@RequestBody @Valid User user) throws EmailAlreadyExistsException {
        return authService.register(user);
    }

    /**
     * loginUser let the user with correct credentials login to use our services.
     *
     * @param request email and password of the user
     * @return user ID and his JWT token
     * @throws ResourceNotFoundException if no such user exists
     */
    @PostMapping("/login")
    public AuthenticationResponse loginUser(@RequestBody AuthenticationRequest request) throws ResourceNotFoundException {
        return authService.login(request);
    }
}
