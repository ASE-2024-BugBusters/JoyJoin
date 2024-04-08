package com.joyjoin.eventservice.service;
import com.joyjoin.eventservice.exception.ResourceNotFoundException;
import com.joyjoin.eventservice.model.Image;
import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.model.ImageUrl;
//import com.joyjoin.eventservice.packer.EventPacker;
import com.joyjoin.eventservice.modelDto.EventDto;
import com.joyjoin.eventservice.packer.EventPacker;
import com.joyjoin.eventservice.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    private final ImageService imageService;
    private final EventPacker eventPacker;

    public EventService(ImageService imageService, EventPacker eventPacker) {
        this.imageService = imageService;
        this.eventPacker = eventPacker;
    }

    @Autowired
    private ModelMapper modelMapper;
    static final String EVENT_BUCKET = "img";
    public Image getImgUploadInformation(LocalDateTime expireTime) {
        String key = String.valueOf(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();
        String uploadUrl = imageService.getPreSignedUrlForUpload(EVENT_BUCKET, key, Duration.between(now, expireTime));
        ImageUrl imageUploadUrl = new ImageUrl(uploadUrl, expireTime);
        return new Image(EVENT_BUCKET, key, List.of(imageUploadUrl));
    }

    public EventDto saveEvent(Event event) {
        if (event.getImages() == null) {
            event.setImages(new ArrayList<>()); // Ensure images is initialized
        }
        if (event.getTags() == null) {
            event.setTags(new ArrayList<>()); // Ensure tags is initialized
        }
        var now = LocalDateTime.now();
        event.setCreatedOn(now);
        event.setLastEdited(now);
        Event savedEvent = eventRepository.save(event);
        return eventPacker.packEvent(savedEvent);
    }
    public List<EventDto> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(eventPacker::packEvent).collect(Collectors.toList());
    }
    @Transactional
    public EventDto getEventById(UUID id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event", "id", id.toString()));
        return eventPacker.packEvent(event);
    }
    public EventDto updateEvent(UUID id, Event eventDetails) {
        var existedEvent = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event", "id", id.toString()));
        modelMapper.map(eventDetails, existedEvent);
        existedEvent.setLastEdited(LocalDateTime.now());
        Event savedEvent = eventRepository.save(existedEvent);
        return eventPacker.packEvent(savedEvent);
    }
}
