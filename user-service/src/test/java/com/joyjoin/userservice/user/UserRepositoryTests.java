package com.joyjoin.userservice.user;

import com.joyjoin.userservice.model.User;
import com.joyjoin.userservice.repository.UserRepository;
import com.joyjoin.userservice.security.model.Role;
import com.joyjoin.userservice.security.model.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Here should only the Repository been tested. This means if there are custom queries on the repository to check if they work
 */
@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");


    @Autowired
    UserRepository userRepository;

    @Test
    public void connectionEstablished() throws Exception {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @BeforeEach
    void setUp() throws Exception {
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role(RoleEnum.USER));
        List<User> users = List.of(User.builder().id(UUID.randomUUID()).firstName("User1").userName("Test").lastName("UserLastName").email("hello@gmail.com").roles(roles).password("12345678").build());
        userRepository.saveAll(users);
    }

    @Test
    void shouldReturnUserByEmail() throws Exception {
        User user = userRepository.findUserByEmail("hello@gmail.com");
        assertThat(user).isNotNull();
    }
}
