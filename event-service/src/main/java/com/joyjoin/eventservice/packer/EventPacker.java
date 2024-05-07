package com.joyjoin.eventservice.packer;

import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.modelDto.EventDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.yaml.snakeyaml.tokens.Token.ID.Tag;

@Component
public class EventPacker {
    private final ImagePacker imagePacker;
    private final ModelMapper modelMapper;

    @Autowired
    public EventPacker(ImagePacker imagePacker, ModelMapper modelMapper) {
        this.imagePacker = imagePacker;
        this.modelMapper = modelMapper;
    }

    public EventDto packEvent(Event event) {
        EventDto res = modelMapper.map(event, EventDto.class);
        res.setImages(imagePacker.packImage(event.getImages(), LocalDateTime.now().plusDays(1)));
        return res;
    }
}
