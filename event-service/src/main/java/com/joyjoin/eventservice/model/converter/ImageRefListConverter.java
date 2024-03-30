package com.joyjoin.eventservice.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyjoin.eventservice.model.ImageRef;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Collections;
import java.util.List;

@Converter
public class ImageRefListConverter implements AttributeConverter<List<ImageRef>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<ImageRef> imageRefList) {
        if (imageRefList == null || imageRefList.isEmpty()) {
            return "[]"; // Return an empty JSON array if the list is empty or null
        }
        try {
            return mapper.writeValueAsString(imageRefList);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }

    @Override
    public List<ImageRef> convertToEntityAttribute(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return mapper.readValue(json, new TypeReference<List<ImageRef>>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }
}
