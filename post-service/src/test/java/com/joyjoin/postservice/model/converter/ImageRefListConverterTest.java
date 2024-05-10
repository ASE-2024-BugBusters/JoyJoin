package com.joyjoin.postservice.model.converter;

import com.joyjoin.postservice.model.ImageRef;
import org.junit.jupiter.api.Test;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = {
        "spring.cloud.discovery.enabled=false",
        "spring.jpa.show-sql=true"
})
public class ImageRefListConverterTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    private final ImageRefListConverter converter = new ImageRefListConverter();

    // 为convertToEntityAttribute测试提供数据
    static Stream<Arguments> convertToEntityAttributeSource() {
        return Stream.of(
                Arguments.of("[]", Collections.emptyList()),
                Arguments.of("[{\"bucket\":\"foo\",\"key\":\"bar\"}]", Collections.singletonList(new ImageRef("foo", "bar"))),
                Arguments.of("[{\"bucket\":\"foo1\",\"key\":\"bar1\"}, {\"bucket\":\"foo2\",\"key\":\"bar2\"}]", Arrays.asList(new ImageRef("foo1", "bar1"), new ImageRef("foo2", "bar2")))
        );
    }

    @ParameterizedTest
    @MethodSource("convertToEntityAttributeSource")
    void testConvertToEntityAttribute(String json, List<ImageRef> expected) {
        assertEquals(expected, converter.convertToEntityAttribute(json));
    }

    // 直接测试convertToDatabaseColumn方法
    @Test
    void testConvertToDatabaseColumn() {
        List<ImageRef> list = Arrays.asList(new ImageRef("foo", "bar"), new ImageRef("foo1", "bar1"));
        String json = converter.convertToDatabaseColumn(list);
        // 因为JSON字符串的生成可能包含空格或格式化的差异，所以这里只测试是否包含了关键内容，而不是直接比较字符串
        assertEquals(true, json.contains("\"bucket\":\"foo\""));
        assertEquals(true, json.contains("\"key\":\"bar\""));
        assertEquals(true, json.contains("\"bucket\":\"foo1\""));
        assertEquals(true, json.contains("\"key\":\"bar1\""));
    }
}
