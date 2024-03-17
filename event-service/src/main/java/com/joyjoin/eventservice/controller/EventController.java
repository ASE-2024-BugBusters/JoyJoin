package com.joyjoin.eventservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.model.Location;
import com.joyjoin.eventservice.modelDTO.EventDTO;
import com.joyjoin.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    private final ObjectMapper objectMapper;

    public EventController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Event> createEvent(
            @RequestParam("event") String eventJson,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        EventDTO eventDto = objectMapper.readValue(eventJson, EventDTO.class);

        Event createdEvent = eventService.createEvent(eventDto, image);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/{eventId}/image")
    public ResponseEntity<byte[]> getEventImage(@PathVariable UUID eventId) {
        Optional<Event> eventOptional = eventService.findById(eventId);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            if (event.getImage() != null && event.getImageContentType() != null) {
                byte[] imageBytes = event.getImage();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType(event.getImageContentType())); // Use the saved MIME type
                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/{eventId}/details")
    public ResponseEntity<?> getEventDetails(@PathVariable UUID eventId) {
        Optional<Event> eventOptional = eventService.findById(eventId);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();

            // Create a map or a DTO to hold the event details you want to return
            Map<String, Object> eventDetails = new HashMap<>();
            eventDetails.put("title", event.getTitle());
            eventDetails.put("time", event.getTime());

            // Location details
            Map<String, String> locationDetails = new HashMap<>();
            Location location = event.getLocation();
            locationDetails.put("street", location.getStreet());
            locationDetails.put("number", location.getNumber());
            locationDetails.put("city", location.getCity());
            locationDetails.put("postalCode", location.getPostalCode());
            locationDetails.put("country", location.getCountry());
            eventDetails.put("location", locationDetails);
            eventDetails.put("participationLimit", event.getParticipationLimit());
            eventDetails.put("description", event.getDescription());
            eventDetails.put("tags", event.getTags());

            return new ResponseEntity<>(eventDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
        }
    }
}
