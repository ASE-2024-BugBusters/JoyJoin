package com.joyjoin.userservice.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyjoin.userservice.model.ImageRef;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public class ImageRefConverter implements AttributeConverter<ImageRef, String> {

    static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ImageRef imageRef) {
        try {
            return mapper.writeValueAsString(imageRef);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    @Override
    public ImageRef convertToEntityAttribute(String s) {
        if (s == null || s.isBlank()) {
            return new ImageRef();
        }
        try {
            return mapper.readValue(s, ImageRef.class);
        } catch (JsonProcessingException e) {
            return new ImageRef();
        }
    }
}
