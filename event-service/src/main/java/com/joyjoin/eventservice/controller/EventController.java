package com.joyjoin.eventservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyjoin.eventservice.model.Event;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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





}
