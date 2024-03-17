package com.joyjoin.userservice.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyjoin.userservice.model.Image;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;


@Converter
public class ImageConverter implements AttributeConverter<Image, String> {

    static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Image image) {
        if (image == null) {
            return "{}";
        }
        try {
            return mapper.writeValueAsString(image);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    @Override
    public Image convertToEntityAttribute(String s) {
        if (s == null || s.isBlank()) {
            return new Image();
        }
        try {
            return mapper.readValue(s, Image.class);
        } catch (JsonProcessingException e) {
            return new Image("", "", List.of());
        }
    }
}
