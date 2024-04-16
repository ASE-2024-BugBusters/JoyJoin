package com.joyjoin.userservice.security.filter;

//import com.joyjoin.userservice.security.service.AuthService;
//import com.joyjoin.userservice.service.UserService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
public class WebSecurity {

//    private AuthService authService;
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    public WebSecurity(AuthService authService, BCryptPasswordEncoder bCryptPasswordEncoder) {
//        this.authService = authService;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//    }
//
//    @Bean
//    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.userDetailsService(authService).passwordEncoder(bCryptPasswordEncoder);
//        return null;
//    }
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

}
