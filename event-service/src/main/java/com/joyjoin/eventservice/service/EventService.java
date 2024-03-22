package com.joyjoin.eventservice.service;
import com.joyjoin.eventservice.controller.dto.UpdateEventRequest;
import com.joyjoin.eventservice.model.Image;
import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.model.ImageUrl;
import com.joyjoin.eventservice.modelDto.EventDto;
import com.joyjoin.eventservice.packer.EventPacker;
import com.joyjoin.eventservice.repository.EventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
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

    public EventDto createEvent(Event event) {
        var now = LocalDateTime.now();
        event.setCreatedOn(now);
        event.setLastEdited(now);
        Event savedEvent = eventRepository.save(event);
        return eventPacker.packEvent(savedEvent);
    }

    private byte[] loadDefaultImageBytes() {
        // Load your default image from the classpath, filesystem, or wherever it is stored
        // For example, if you have a default image in the resources folder:
        Resource resource = new ClassPathResource("static/images/event_default.jpeg");
        try {
            return StreamUtils.copyToByteArray(resource.getInputStream());
        } catch (IOException e) {
            // Handle the exception, possibly rethrowing as a runtime exception or logging a warning
            throw new RuntimeException("Could not load default image", e);
        }
    }
    public List<EventDto> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        if (events.isEmpty()) {
            throw new RuntimeException("Event not found");
        }
        return events.stream().map(eventPacker::packEvent).collect(Collectors.toList());
    }

    public EventDto getEventById(UUID id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        return eventPacker.packEvent(event);
    }
    public void deleteEvent(UUID id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
//        event.setIsDeleted(true);
        eventRepository.save(event);
    }


    public EventDto updateEvent(UUID eventId, UpdateEventRequest updateEventRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
        modelMapper.map(updateEventRequest, event);
        event.setLastEdited(LocalDateTime.now());
        Event updatedEvent = eventRepository.save(event);
        return eventPacker.packEvent(updatedEvent);
    }


    static final String AVATAR_BUCKET = "img";
    public Image getImgUploadInformation(UUID uuid, LocalDateTime expireTime) {
        String key = uuid.toString() + "--" + UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        String uploadUrl = imageService.getPreSignedUrlForUpload("img", key, Duration.between(now, expireTime));
        ImageUrl imageUploadUrl = new ImageUrl(uploadUrl, expireTime);
        return new Image(AVATAR_BUCKET, key, List.of(imageUploadUrl));
    }
}
