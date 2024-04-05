package com.joyjoin.userservice.security.service;

import com.joyjoin.userservice.exception.EmailAlreadyExistsException;
import com.joyjoin.userservice.exception.ErrorCode;
import com.joyjoin.userservice.security.model.Role;
import com.joyjoin.userservice.model.User;
import com.joyjoin.userservice.repository.UserRepository;
import com.joyjoin.userservice.security.model.AuthenticationRequest;
import com.joyjoin.userservice.security.model.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(User user){
        User optionalUser = repository.findUserByEmail(user.getEmail());
        if (optionalUser != null) {
            throw new EmailAlreadyExistsException(ErrorCode.USER_EMAIL_ALREADY_EXISTS.getErrorCode());
        }
        LocalDateTime now = LocalDateTime.now();
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role("USER"));
        User userToSave = User.builder()
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(roles)
                .createdOn(now)
                .lastEdited(now)
                .build();
        repository.save(userToSave);
        var jwtToken = jwtService.generateToken(userToSave);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = repository.findUserByEmail(request.getEmail());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }


}
