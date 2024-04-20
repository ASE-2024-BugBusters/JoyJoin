package com.joyjoin.eventservice.controller;
import com.joyjoin.eventservice.controller.dto.GetImgUploadUrlResponse;
import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.modelDto.EventDto;
import com.joyjoin.eventservice.modelDto.PostEventRequest;
import com.joyjoin.eventservice.modelDto.UpdateEventRequest;
import com.joyjoin.eventservice.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin(allowedHeaders = "*", originPatterns = "/**")
@RequestMapping("api/events")
public class EventController {

    @Autowired
    private EventService eventService;
    private final ModelMapper modelMapper;

    public EventController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody PostEventRequest request) {
        Event event = modelMapper.map(request, Event.class);
        EventDto createdEvent = eventService.saveEvent(event);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }
    @GetMapping("/get_upload_image_url")
    public GetImgUploadUrlResponse getImgUploadUrl() {
        var expireTime = LocalDateTime.now().plusMinutes(30);
        return new GetImgUploadUrlResponse(eventService.getImgUploadInformation(expireTime));
    }
    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents() {
        List<EventDto> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable UUID id) {
        EventDto event = eventService.getEventById(id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable UUID id, @RequestBody UpdateEventRequest request) {
        Event event = modelMapper.map(request, Event.class);
        return new ResponseEntity<>(eventService.updateEvent(id, event), HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
