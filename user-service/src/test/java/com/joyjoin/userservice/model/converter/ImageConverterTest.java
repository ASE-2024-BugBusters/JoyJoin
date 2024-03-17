package com.joyjoin.userservice.model.converter;

import com.joyjoin.userservice.model.Image;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class ImageConverterTest {
    private final ImageConverter converter = new ImageConverter();

    @ParameterizedTest
    @MethodSource
    void TestConvertToEntityAttribute(String from, Image expected) {
        assertEquals(expected, converter.convertToEntityAttribute(from));
    }

    static Stream<Arguments> TestConvertToEntityAttribute() {
        return Stream.of(
                arguments(null, new Image()),
                arguments("", new Image()),
                arguments("  \t\r\n", new Image()),
                arguments("null", null),
                arguments("{}", new Image()),
                arguments(
                        "{\"bucket\":\"foo\",\"key\":\"bar\",\"urls\":[\"https://example.com/foo/bar.jpg\",\"https://example.com/foo/bar.png\"]}",
                        new Image("foo", "bar", List.of("https://example.com/foo/bar.jpg", "https://example.com/foo/bar.png"))
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void TestConvertToDatabaseColumn(Image from, String expected) {
        assertEquals(expected, converter.convertToDatabaseColumn(from));
    }

    static Stream<Arguments> TestConvertToDatabaseColumn() {
        return Stream.of(
                arguments(null, "null"),
                arguments(new Image(), "{\"bucket\":\"\",\"key\":\"\",\"urls\":[],\"uri\":\"\"}"),
                arguments(
                        new Image("foo", "bar", List.of("https://example.com/foo/bar.jpg", "https://example.com/foo/bar.png")),
                        "{\"bucket\":\"foo\",\"key\":\"bar\",\"urls\":[\"https://example.com/foo/bar.jpg\",\"https://example.com/foo/bar.png\"],\"uri\":\"foo/bar\"}"
                )
        );
    }
}
