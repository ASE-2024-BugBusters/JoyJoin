package com.joyjoin.eventservice.service;

import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.model.Location;
import com.joyjoin.eventservice.modelDTO.EventDTO;
import com.joyjoin.eventservice.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StreamUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event createEvent(EventDTO eventDto, MultipartFile image) throws IOException {
        Event event = new Event();
        event.setTitle(eventDto.getTitle());
        event.setTime(eventDto.getTime());

        // Map LocationDTO to Location entity
        Location location = new Location(
                eventDto.getLocation().getStreet(),
                eventDto.getLocation().getNumber(),
                eventDto.getLocation().getCity(),
                eventDto.getLocation().getPostalCode(),
                eventDto.getLocation().getCountry()
        );
        event.setLocation(location);

        event.setParticipationLimit(eventDto.getParticipationLimit());
        event.setDescription(eventDto.getDescription());
        event.setTags(eventDto.getTags());
        if (image != null && !image.isEmpty()) {
            event.setImage(image.getBytes());
            event.setImageContentType(image.getContentType());
        } else {
            // Set default image bytes if there is no image provided
            event.setImage(loadDefaultImageBytes());
            event.setImageContentType("image/jpeg");
        }
        return eventRepository.save(event);
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
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    public Optional<Event> findById(UUID id) {
        return eventRepository.findById(id);
    }
}
