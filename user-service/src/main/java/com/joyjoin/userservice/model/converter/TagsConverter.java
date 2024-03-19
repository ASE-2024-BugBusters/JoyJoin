package com.joyjoin.userservice.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyjoin.userservice.model.InterestTag;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@Converter
public class TagsConverter implements AttributeConverter<List<InterestTag>, String> {

    static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<InterestTag> from) {
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
    public List<InterestTag> convertToEntityAttribute(String from) {
        if (from == null || from.isBlank()) {
            return List.of();
        }

        List<InterestTag> interestTags = new ArrayList<>();
        try {
            var tagTextObjectList = mapper.readValue(from, interestTags.getClass());
            if (tagTextObjectList == null) {
                return List.of();
            }
            for (Object tagTextObj : tagTextObjectList) {
                interestTags.add(InterestTag.valueOf((String) tagTextObj));
            }
        } catch (JsonProcessingException e) {
            return interestTags;
        }
        return interestTags;
    }
}
