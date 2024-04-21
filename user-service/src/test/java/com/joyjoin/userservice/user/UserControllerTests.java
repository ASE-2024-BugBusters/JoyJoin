package com.joyjoin.userservice.user;

import com.joyjoin.userservice.model.User;
import com.joyjoin.userservice.repository.UserRepository;
import com.joyjoin.userservice.security.model.AuthenticationResponse;
import com.joyjoin.userservice.security.model.Role;
import com.joyjoin.userservice.security.model.RoleEnum;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.service.RequestBodyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class UserControllerTests {

    // TODO the integration tests do not work now, since we need to make request through
    //  the API-GATEWAY or exclude it somehow and then us JWT to make requests otherwise it fails

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    MockMvc mockMvc;
    private String token;

    @Autowired
    UserRepository userRepository;
    AuthenticationResponse authenticationResponse;
    @Autowired
    private RequestBodyService requestBodyBuilder;

    @BeforeEach
    void setUp() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post("http://localhost:9191/user-service/api/auth/register"));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        this.token = "Bearer " + json.getString("token");
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role(RoleEnum.USER));
        List<User> users = List.of(User.builder().id(UUID.randomUUID()).firstName("User1").userName("Test").lastName("UserLastName").email("hello@gmail.com").roles(roles).password("12345678").build());
        userRepository.saveAll(users);
    }

    @Test
    void shouldFindAllUsers() throws Exception {
        // /api/users
        this.mockMvc.perform(get("http://localhost:9191/user-service/api/user").accept(MediaType.APPLICATION_JSON).header("Authentication", this.token));
        System.out.println("heeeeeeeeeee");
    }

}
