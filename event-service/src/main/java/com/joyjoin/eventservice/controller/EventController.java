package com.joyjoin.eventservice.controller;

import com.joyjoin.eventservice.controller.dto.GetImgUploadUrlResponse;
import com.joyjoin.eventservice.controller.dto.UpdateEventRequest;
import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.modelDto.EventDto;
import com.joyjoin.eventservice.service.EventService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;
    private final ModelMapper modelMapper;

    public EventController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * For testing purposes
     * @param eventDto
     * @return Event
     */
    @PostMapping()
    public ResponseEntity<Event> createEvent(@Valid @RequestBody EventDto eventDto) {
        Event event = modelMapper.map(eventDto, Event.class);
        EventDto createdEventDto = eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }
//    @GetMapping()
//    public EventDto getAllEvents() {
//        return EventDto eventService.getAllEvents();
//    }
    @GetMapping()
    public ResponseEntity<List<EventDto>> getAllEvents() {
        List<EventDto> eventDtos = eventService.getAllEvents();
        return new ResponseEntity<>(eventDtos, HttpStatus.OK);
    }

    private EventDto convertToDto(Event event) {
        EventDto eventDto = new EventDto();
        modelMapper.map(event, EventDto.class);
        return eventDto;
    }
    @PostMapping("/publish")
    public ResponseEntity<EventDto> publishEvent(@RequestBody Event event) {
        EventDto createdEventDto = eventService.createEvent(event);
        return new ResponseEntity<>(createdEventDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{eventId}")
    public EventDto updateEvent(@PathVariable UUID eventId, @RequestBody UpdateEventRequest request) {
        eventService.updateEvent(eventId, request);
        return null;
    }


    @GetMapping("/{eventId}/upload_image")
    public GetImgUploadUrlResponse getImgUploadUrl(@PathVariable UUID eventId) {
        var expireTime = LocalDateTime.now().plusMinutes(30);
        return new GetImgUploadUrlResponse(eventService.getImgUploadInformation(eventId, expireTime));
    }
    @GetMapping("/{eventId}")
    public EventDto getEventByUUID(@PathVariable UUID eventId) {
        return eventService.getEventById(eventId);
    }
}
