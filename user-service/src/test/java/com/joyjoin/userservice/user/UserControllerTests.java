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
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.HttpHeaders;
import java.util.Date;
import java.util.Objects;

import static com.joyjoin.userservice.Util.Util.asJsonString;
import static com.joyjoin.userservice.Util.Util.stringToObj;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@TestPropertySource(properties = {
        "spring.cloud.discovery.enabled=false",
        "spring.jpa.show-sql=true"
})
public class UserControllerTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

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
    void connectionEstablished() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
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
}