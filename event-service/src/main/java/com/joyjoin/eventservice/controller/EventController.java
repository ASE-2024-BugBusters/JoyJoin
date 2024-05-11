package com.joyjoin.eventservice.controller;

import com.joyjoin.eventservice.controller.dto.GetImgUploadUrlResponse;
import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.modelDto.EventDto;
import com.joyjoin.eventservice.modelDto.EventRegistrationDto;
import com.joyjoin.eventservice.modelDto.PostEventRequest;
import com.joyjoin.eventservice.modelDto.UpdateEventRequest;
import com.joyjoin.eventservice.service.EventRegistrationService;
import com.joyjoin.eventservice.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles all web requests for operations related to events,
 * including creation, modification, deletion, and retrieval of events,
 * as well as managing event registrations.
 */
@RestController
@CrossOrigin(allowedHeaders = "*", originPatterns = "/**")
@RequestMapping("api/events")
public class EventController {
    private final EventService eventService;
    private final ModelMapper modelMapper;
    private final EventRegistrationService eventRegistrationService;

    /**
     * Constructs an instance with required services for event management.
     *
     * @param eventService the business logic service for event operations
     * @param modelMapper tool for converting entities to DTOs and vice versa
     * @param eventRegistrationService handles the registration of users to events
     */
    @Autowired
    public EventController(EventService eventService, ModelMapper modelMapper, EventRegistrationService eventRegistrationService) {
        this.eventService = eventService;
        this.modelMapper = modelMapper;
        this.eventRegistrationService = eventRegistrationService;
    }

    /**
     * Creates an event based on request data.
     *
     * @param request Details for creating a new event
     * @return A ResponseEntity containing the newly created EventDto and HTTP status
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@Valid @RequestBody PostEventRequest request) {
        Event event = modelMapper.map(request, Event.class);
        EventDto createdEvent = eventService.saveEvent(event);
        return createdEvent;
    }

    /**
     * Generates and retrieves a pre-signed image upload URL with a 30-minute expiry.
     *
     * @return The upload URL and expiration details
     */
    @GetMapping("/get_upload_image_url")
    public GetImgUploadUrlResponse getImgUploadUrl() {
        var expireTime = LocalDateTime.now().plusMinutes(30);
        return new GetImgUploadUrlResponse(eventService.getImgUploadInformation(expireTime));
    }

    /**
     * Fetches events filtered by specified criteria such as title, city, date, and tags.
     * Optionally excludes fully booked events.
     *
     * @param title Optional title filter
     * @param city Optional city filter
     * @param date Optional date filter
     * @param tags Optional tags filter
     * @param excludeFullEvents If true, fully booked events are excluded
     * @return A list of events matching the specified criteria
     */
    @GetMapping("/filter")
    public ResponseEntity<List<EventDto>> getFilteredEvents(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String tags,
            @RequestParam(defaultValue = "false") boolean excludeFullEvents) {

        LocalDate eventDate = null;
        if (date != null && !date.isEmpty()) {
            eventDate = LocalDate.parse(date);
        }
        List<String> tagList = tags != null ? Arrays.stream(tags.split(",")).map(String::trim).collect(Collectors.toList()) : null;

        List<EventDto> eventsDto = eventService.getFilteredEvents(title, city, eventDate, tagList, excludeFullEvents);
        return ResponseEntity.ok(eventsDto);
    }

    /**
     * Retrieves all currently active events.
     *
     * @return All active events wrapped in a ResponseEntity
     */
    @GetMapping("/all")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        List<EventDto> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    /**
     * Gets the details of a specific event by its ID.
     *
     * @param eventId The UUID of the event to retrieve
     * @return The EventDto if found, wrapped in a ResponseEntity
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(@PathVariable UUID eventId) {
        EventDto event = eventService.getEventById(eventId);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    /**
     * Updates an event's details based on the provided data.
     *
     * @param eventId The UUID of the event to update
     * @param request The new details for the event
     * @return The updated EventDto wrapped in a ResponseEntity
     */
    @PatchMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable UUID eventId, @RequestBody UpdateEventRequest request) {
        Event event = modelMapper.map(request, Event.class);
        return new ResponseEntity<>(eventService.updateEvent(eventId, event), HttpStatus.OK);
    }

    /**
     * Updates the images associated with an event.
     *
     * @param eventId The UUID of the event to update
     * @param request Contains new image details for the event
     * @return The updated EventDto with new images
     */
    @PatchMapping("/{eventId}/images")
    public ResponseEntity<EventDto> updateImages(@PathVariable UUID eventId, @RequestBody UpdateEventRequest request) {
        Event event = modelMapper.map(request, Event.class);
        return new ResponseEntity<>(eventService.updateImages(eventId, event), HttpStatus.OK);
    }

    /**
     * Deletes an event by its ID.
     *
     * @param eventId The UUID of the event to delete
     * @return The EventDto of the deleted event, wrapped in a ResponseEntity
     */
    @DeleteMapping("/{eventId}")
    public ResponseEntity<EventDto> deleteEvent(@PathVariable UUID eventId) {
        EventDto deletedEvent = eventService.deleteEvent(eventId);
        return new ResponseEntity<>(deletedEvent, HttpStatus.OK);
    }

    /**
     * Registers a user to an event using their respective UUIDs.
     *
     * @param eventId The UUID of the event
     * @param userId The UUID of the user
     * @return The EventRegistrationDto after registration, wrapped in a ResponseEntity
     */
    @PostMapping("/{eventId}/register/{userId}")
    public ResponseEntity<EventRegistrationDto> registerEvent(@PathVariable UUID eventId, @PathVariable UUID userId) {
        return new ResponseEntity<>(eventRegistrationService.registerUserToEvent(eventId, userId), HttpStatus.OK);
    }

    /**
     * Unregisters a user from an event using their respective UUIDs.
     *
     * @param eventId The UUID of the event
     * @param userId The UUID of the user
     * @return The EventRegistrationDto after unregistration, wrapped in a ResponseEntity
     */
    @DeleteMapping("/{eventId}/remove/{userId}")
    public ResponseEntity<EventRegistrationDto> unregisterEvent(@PathVariable UUID eventId, @PathVariable UUID userId) {
        return new ResponseEntity<>(eventRegistrationService.removeUserToEvent(eventId, userId), HttpStatus.OK);
    }

    /**
     * Retrieves a list of participants' UUIDs for a specified event.
     *
     * @param eventId The UUID of the event to query
     * @return A list of participant UUIDs
     */
    @GetMapping("/{eventId}/participants")
    public List<UUID> getParticipantsByEventId(@PathVariable UUID eventId) {
        List<UUID> users = eventRegistrationService.getParticipantsByEventId(eventId);
        return users;
    }

    /**
     * Retrieves a list of events that a specific user has registered for.
     *
     * @param userId The UUID of the user whose event registrations are queried
     * @return A list of EventDto objects representing the events the user is registered for
     */
    @GetMapping("/by-userId")
    public ResponseEntity<List<EventDto>> getEventsByUserId(@RequestParam UUID userId) {
        List<EventDto> events = eventRegistrationService.getEventsByUserId(userId);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    /**
     * Simple endpoint to test if the service is operational.
     *
     * @return A string "test" indicating the service is operational
     */
    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
