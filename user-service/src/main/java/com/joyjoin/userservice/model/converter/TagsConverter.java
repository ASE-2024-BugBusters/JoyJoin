package com.joyjoin.userservice.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyjoin.userservice.model.InterestTag;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

@Converter
public class TagsConverter implements AttributeConverter<List<InterestTag>, String> {
    @Override
    public String convertToDatabaseColumn(List<InterestTag> from) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(from);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }

    @Override
    public List<InterestTag> convertToEntityAttribute(String from) {
        if (from == null || from.isBlank()) {
            return List.of();
        }
        ObjectMapper mapper = new ObjectMapper();
        List<InterestTag> interestTags = new ArrayList<>();
        try {
            for (Object tagTextObj : mapper.readValue(from, interestTags.getClass())) {
                interestTags.add(InterestTag.valueOf((String) tagTextObj));
            }
        } catch (JsonProcessingException e) {
            return interestTags;
        }
        return interestTags;
    }
}
