package com.joyjoin.userservice.model.converter;

import com.joyjoin.userservice.model.InterestTag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class TagsConverterTest {

    private final TagsConverter converter = new TagsConverter();

    @ParameterizedTest
    @MethodSource
    void TestConvertToEntityAttribute(String from, List<InterestTag> expect) {
        assertEquals(converter.convertToEntityAttribute(from), expect);
    }

    static Stream<Arguments> TestConvertToEntityAttribute() {
        return Stream.of(
                arguments("[]", new ArrayList<>()),
                arguments("[\"VeganCuisine\"]", List.of(InterestTag.VeganCuisine)),
                arguments("[\"Basketball\",\"HealthyEating\"]", List.of(InterestTag.Basketball, InterestTag.HealthyEating))
        );
    }

    @ParameterizedTest
    @MethodSource
    void TestConvertToDatabaseColumn(List<InterestTag> from, String expect) {
        assertEquals(converter.convertToDatabaseColumn(from), expect);
    }

    static Stream<Arguments> TestConvertToDatabaseColumn() {
        return Stream.of(
                arguments(List.of(), "[]"),
                arguments(List.of(InterestTag.VeganCuisine), "[\"VeganCuisine\"]"),
                arguments(List.of(InterestTag.Basketball, InterestTag.HealthyEating), "[\"Basketball\",\"HealthyEating\"]")
        );
    }
}
