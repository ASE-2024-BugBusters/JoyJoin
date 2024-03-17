package com.joyjoin.userservice.model.converter;

import com.joyjoin.userservice.model.ImageRef;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class ImageRefConverterTest {
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
