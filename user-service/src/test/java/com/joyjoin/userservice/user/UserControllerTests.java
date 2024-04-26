package com.joyjoin.userservice.user;

import com.joyjoin.userservice.model.User;
import com.joyjoin.userservice.repository.UserRepository;
import com.joyjoin.userservice.security.model.AuthenticationRequest;
import com.joyjoin.userservice.security.model.AuthenticationResponse;
import com.joyjoin.userservice.security.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import java.util.Date;
import java.util.Objects;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc

@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:13.2-alpine://user-service",
        "spring.cloud.discovery.enabled=false"
})
public class UserControllerTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @Autowired
    MockMvc mockMvc;

    private final String AUTHORIZATION_PREFIX = "Bearer ";

    @BeforeEach
    void setUp() {
        if (userRepository.count() == 0) {
            for (int i = 0; i < 5; i++) {
                User user = User.builder()
                        .firstName("testFirstName" + i)
                        .userName("testUserName" + i)
                        .lastName("testLastName" + i)
                        .email("test@email.com" + i)
                        .password("12345678")
                        .birthDate(new Date()).build();
                this.authService.register(user);
            }
        }
    }

    @Test
    void loginUser() throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder().email("test@email.com0").password("12345678").build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login").content(Objects.requireNonNull(asJsonString(authenticationRequest))).contentType("application/json").accept("application/json")).andExpect(status().isOk()).andReturn();
    }

    @Test
    void getUsers() throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder().email("test@email.com1").password("12345678").build();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login").content(Objects.requireNonNull(asJsonString(authenticationRequest))).contentType("application/json").accept("application/json")).andExpect(status().isOk()).andReturn();
        AuthenticationResponse response = stringToObj(mvcResult, AuthenticationResponse.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user").header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_PREFIX + response.getToken()).accept("application/json")).andExpect(status().isOk());
    }

    /**
     * Is used to transform the Object into a JSON String
     * @param obj
     * @return
     */
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param mvcResult
     * @param objClass
     * @return
     * @param <T>
     * @throws IOException
     */
    private <T> T stringToObj(MvcResult mvcResult, Class<T> objClass) throws IOException {
        String content = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(content, objClass);
    }
}
