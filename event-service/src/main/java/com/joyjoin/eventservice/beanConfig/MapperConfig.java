package com.joyjoin.eventservice.beanConfig;

import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.model.ImageRef;
import com.joyjoin.eventservice.modelDto.UpdateEventRequest;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        // Creating a TypeMap to handle specific property mappings
//        TypeMap<UpdateEventRequest, Event> eventTypeMap = modelMapper.createTypeMap(UpdateEventRequest.class, Event.class);
//        eventTypeMap.addMappings(mapper -> {
//            mapper.map(UpdateEventRequest::getImages, (dest, v) -> {
//                List<ImageRef> srcImages = (List<ImageRef>) v; // Explicit casting
//                if (dest.getImages() == null) {
//                    dest.setImages(new ArrayList<>()); // Ensure the destination list is initialized
//                }
//                dest.getImages().clear(); // Clear existing images first
//                dest.getImages().addAll(srcImages); // Add all from the source
//            });
//        });

        return modelMapper;
    }
}
