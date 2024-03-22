package com.joyjoin.eventservice.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyjoin.eventservice.model.Tag;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.HashSet;
import java.util.Set;

@Converter
public class TagsConverter implements AttributeConverter<Set<Tag>, String> {

    static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set<Tag> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]";
        }
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }

    @Override
    public Set<Tag> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return new HashSet<>();
        }

        Set<Tag> tags = new HashSet<>();
        try {
            // You need to create a TypeReference for Set<Tag> because Java type erasure
            Set<String> tagTextSet = mapper.readValue(dbData, mapper.getTypeFactory().constructCollectionType(Set.class, String.class));
            for (String tagText : tagTextSet) {
                tags.add(Tag.valueOf(tagText));
            }
        } catch (JsonProcessingException e) {
            return new HashSet<>();
        }
        return tags;
    }
}
