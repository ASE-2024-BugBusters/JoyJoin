package com.joyjoin.userservice.model.converter;

import com.joyjoin.userservice.model.InterestTag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = {
        "spring.cloud.discovery.enabled=false",
        "spring.jpa.show-sql=true"
})
public class TagsConverterTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    private final TagsConverter converter = new TagsConverter();

    @ParameterizedTest
    @MethodSource
    void TestConvertToEntityAttribute(String from, List<InterestTag> expected) {
        assertEquals(expected, converter.convertToEntityAttribute(from));
    }

    static Stream<Arguments> TestConvertToEntityAttribute() {
        return Stream.of(
                arguments(null, List.of()),
                arguments("", List.of()),
                arguments(" \t\r\n", List.of()),
                arguments("[]", List.of()),
                arguments("[\"VeganCuisine\"]", List.of(InterestTag.VeganCuisine)),
                arguments("[\"Basketball\",\"HealthyEating\"]", List.of(InterestTag.Basketball, InterestTag.HealthyEating))
        );
    }

    @ParameterizedTest
    @MethodSource
    void TestConvertToDatabaseColumn(List<InterestTag> from, String expected) {
        assertEquals(expected, converter.convertToDatabaseColumn(from));
    }

    static Stream<Arguments> TestConvertToDatabaseColumn() {
        return Stream.of(
                arguments(List.of(), "[]"),
                arguments(List.of(InterestTag.VeganCuisine), "[\"VeganCuisine\"]"),
                arguments(List.of(InterestTag.Basketball, InterestTag.HealthyEating), "[\"Basketball\",\"HealthyEating\"]")
        );
    }
}
