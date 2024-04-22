package com.joyjoin.eventservice.service;
import com.joyjoin.eventservice.exception.ResourceNotFoundException;
import com.joyjoin.eventservice.model.Image;
import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.model.ImageUrl;
import com.joyjoin.eventservice.modelDto.EventDto;
import com.joyjoin.eventservice.packer.EventPacker;
import com.joyjoin.eventservice.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ImageService imageService;
    private final EventPacker eventPacker;
    private final ModelMapper modelMapper;
    static final String EVENT_BUCKET = "img";

    @Autowired
    public EventService(EventRepository eventRepository, ImageService imageService, EventPacker eventPacker, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.imageService = imageService;
        this.eventPacker = eventPacker;
        this.modelMapper = modelMapper;
    }

    public Image getImgUploadInformation(LocalDateTime expireTime) {
        String key = String.valueOf(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();
        String uploadUrl = imageService.getPreSignedUrlForUpload(EVENT_BUCKET, key, Duration.between(now, expireTime));
        ImageUrl imageUploadUrl = new ImageUrl(uploadUrl, expireTime);
        return new Image(EVENT_BUCKET, key, List.of(imageUploadUrl));
    }
    @Transactional
    public EventDto saveEvent(Event event) {
//        if (event.getImages() == null) {
//            event.setImages(new ArrayList<>()); // Ensure images is initialized
//        }
//        if (event.getTags() == null) {
//            event.setTags(new ArrayList<>()); // Ensure tags is initialized
//        }
        Event savedEvent = eventRepository.save(event);
        return eventPacker.packEvent(savedEvent);
    }
    @Transactional
    public List<EventDto> getAllEvents() {
        List<Event> events = eventRepository.findByIsDeletedFalse();
        return events.stream().map(eventPacker::packEvent).collect(Collectors.toList());
    }
    @Transactional
    public EventDto getEventById(UUID eventId) {
        System.out.println("here is the id in service");
        System.out.println(eventId);
        Event event = eventRepository.findByEventIdAndIsDeletedFalse(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId.toString(),
                        Collections.singletonList("This event may have been deleted or does not exist.")));
        return eventPacker.packEvent(event);
    }
    @Transactional
    public EventDto updateEvent(UUID eventId, Event eventDetails) {
        var existedEvent = eventRepository.findByEventIdAndIsDeletedFalse(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId.toString(),
                        Collections.singletonList("This event may have been deleted or does not exist.")));
        modelMapper.map(eventDetails, existedEvent);
        Event savedEvent = eventRepository.save(existedEvent);
        return eventPacker.packEvent(savedEvent);
    }
    @Transactional
    public EventDto deleteEvent(UUID eventId) {
        Event event = eventRepository.findByEventIdAndIsDeletedFalse(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId.toString(),
                        Collections.singletonList("This event may have been deleted or does not exist.")));
        event.setDeleted(true);
        eventRepository.save(event);
        return modelMapper.map(event, EventDto.class);
    }
}
