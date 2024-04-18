package com.joyjoin.userservice.security.service;

import com.joyjoin.userservice.exception.EmailAlreadyExistsException;
import com.joyjoin.userservice.exception.ErrorCode;
import com.joyjoin.userservice.security.model.*;
import com.joyjoin.userservice.model.User;
import com.joyjoin.userservice.repository.UserRepository;
import com.joyjoin.userservice.security.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    /**
     *
     * @param user which contains all the information to create a new one (firstName, lastName, birthDate, email, password)
     * @return <b>AuthenticationResponse</b> which contains the JWT Token as a String
     */
    public AuthenticationResponse register(User user){
        User optionalUser = repository.findUserByEmail(user.getEmail());
        if (optionalUser != null) {
            throw new EmailAlreadyExistsException(ErrorCode.USER_EMAIL_ALREADY_EXISTS.getErrorCode());
        }
        LocalDateTime now = LocalDateTime.now();
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role(RoleEnum.USER));
        User userToSave = User.builder()
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(roles)
                .createdOn(now)
                .lastEdited(now)
                .build();
        User savedUser = repository.save(userToSave);
        var jwtToken = jwtService.generateToken(userToSave);
        deleteAllUserTokens(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder().userId(savedUser.getId()).token(jwtToken).build();
    }


    /**
     *
     * @param request <b>AuthenticationRequest</b> contains the user Credentials to create a valid JWT Token
     * @return <b>AuthenticationResponse</b> which contains the JWT Token as a String
     */
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = repository.findUserByEmail(request.getEmail());
        var jwtToken = jwtService.generateToken(user);
        deleteAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder().userId(user.getId()).token(jwtToken).build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .token(jwtToken)
                .user(user)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    /**
     * Only one JWT is allowed to make requests, when it expires a new one needs to be created
     * @param user
     */
    private void deleteAllUserTokens(User user) {
        List<Token> validTokens = tokenRepository.findAllValidTokenByUserId(user.getId());
        if (validTokens.isEmpty()) {
            return;
        }
        tokenRepository.deleteAll();
    }
}
