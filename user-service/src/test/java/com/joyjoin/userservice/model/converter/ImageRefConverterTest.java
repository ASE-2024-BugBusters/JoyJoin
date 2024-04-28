package com.joyjoin.userservice.model.converter;

import com.joyjoin.userservice.model.ImageRef;
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

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = {
        "spring.cloud.discovery.enabled=false",
        "spring.jpa.show-sql=true"
})
public class ImageRefConverterTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    private final ImageRefConverter converter = new ImageRefConverter();


    @ParameterizedTest
    @MethodSource
    void TestConvertToEntityAttribute(String from, ImageRef expected) {
        assertEquals(expected, converter.convertToEntityAttribute(from));
    }

    static Stream<Arguments> TestConvertToEntityAttribute() {
        return Stream.of(
                arguments(null, new ImageRef()),
                arguments("", new ImageRef()),
                arguments("  \t\r\n", new ImageRef()),
                arguments("null", null),
                arguments("{}", new ImageRef()),
                arguments("{\"bucket\":\"foo\",\"key\":\"bar\"}", new ImageRef("foo", "bar"))
        );
    }

    @ParameterizedTest
    @MethodSource
    void TestConvertToDatabaseColumn(ImageRef from, String expected) {
        assertEquals(expected, converter.convertToDatabaseColumn(from));
    }

    static Stream<Arguments> TestConvertToDatabaseColumn() {
        return Stream.of(
                arguments(null, "null"),
                arguments(new ImageRef(), "{\"bucket\":\"\",\"key\":\"\"}"),
                arguments(new ImageRef("foo", "bar"), "{\"bucket\":\"foo\",\"key\":\"bar\"}")
        );
    }
}
