package com.joyjoin.eventservice.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyjoin.eventservice.model.Tag;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter
public class TagsConverter implements AttributeConverter<List<Tag>, String> {

    static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Tag> from) {
        if (from == null) {
            from = List.of();
        }
        try {
            return mapper.writeValueAsString(from);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }

    @Override
    public List<Tag> convertToEntityAttribute(String from) {
        if (from == null || from.isBlank()) {
            return List.of();
        }

        List<Tag> tags = new ArrayList<>();
        try {
            var tagTextObjectList = mapper.readValue(from, tags.getClass());
            if (tagTextObjectList == null) {
                return List.of();
            }
            for (Object tagTextObj : tagTextObjectList) {
                tags.add(Tag.valueOf((String) tagTextObj));
            }
        } catch (JsonProcessingException e) {
            return tags;
        }
        return tags;
    }
}
