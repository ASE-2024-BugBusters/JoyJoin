package com.joyjoin.eventservice.controller;

import com.joyjoin.eventservice.controller.dto.GetImgUploadUrlResponse;
import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.model.EventRegistration;
import com.joyjoin.eventservice.model.Rating;
import com.joyjoin.eventservice.modelDto.EventDto;
import com.joyjoin.eventservice.modelDto.EventRegistrationDto;
import com.joyjoin.eventservice.modelDto.PostEventRequest;
import com.joyjoin.eventservice.modelDto.UpdateEventRequest;
import com.joyjoin.eventservice.service.EventRegistrationService;
import com.joyjoin.eventservice.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller class for handling all event-related actions.
 * This class handles requests to create, update, delete, and fetch events,
 * as well as register or unregister users from events.
 */
@RestController
@CrossOrigin(allowedHeaders = "*", originPatterns = "/**")
@RequestMapping("api/events")
public class EventController {
    private final EventService eventService;
    private final ModelMapper modelMapper;
    private  final EventRegistrationService eventRegistrationService;

    /**
     * Constructs an EventController with the specified EventService and ModelMapper.
     *
     * @param eventService             the service to handle the event logic
     * @param modelMapper              the tool to map between DTOs and entities
     * @param eventRegistrationService the service to handle the event registration logic
     */
    @Autowired
    public EventController(EventService eventService, ModelMapper modelMapper, EventRegistrationService eventRegistrationService) {
        this.eventService = eventService;
        this.modelMapper = modelMapper;
        this.eventRegistrationService = eventRegistrationService;
    }

    /**
     * Creates a new event based on the provided request data.
     *
     * @param request the event creation request with event details
     * @return the created event data transfer object
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@Valid @RequestBody PostEventRequest request) {
        Event event = modelMapper.map(request, Event.class);
        EventDto createdEvent = eventService.saveEvent(event);
        return createdEvent;
    }

    /**
     * Provides an upload URL for an event image that expires in 30 minutes.
     *
     * @return the response containing the URL and expiration information
     */
    @GetMapping("/get_upload_image_url")
    public GetImgUploadUrlResponse getImgUploadUrl() {
        var expireTime = LocalDateTime.now().plusMinutes(30);
        return new GetImgUploadUrlResponse(eventService.getImgUploadInformation(expireTime));
    }

    /**
     * Retrieves a list of events filtered based on optional criteria including title, city, date, tags, and whether to exclude full events.
     *
     * @param title Optional parameter to filter events by their title. If provided, will match events containing this substring.
     * @param city Optional parameter to filter events by city. Events in the specified city will be returned.
     * @param date Optional parameter to filter events by date. Only events on this specific date will be returned.
     * @param tags Optional comma-separated list of tags to filter events. Only events containing these tags will be returned.
     * @param excludeFullEvents Boolean flag to determine if full events should be excluded from the results. Default is false, meaning full events will be included.
     * @return ResponseEntity containing a list of EventDto objects that match the filters provided. Returns an OK (200) HTTP status code.
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
            eventDate = LocalDate.parse(date); // Converts string date to LocalDate object.
        }
        List<String> tagList = tags != null ? Arrays.stream(tags.split(",")).map(String::trim).collect(Collectors.toList()) : null;

        List<EventDto> eventsDto = eventService.getFilteredEvents(title, city, eventDate, tagList, excludeFullEvents);
        return ResponseEntity.ok(eventsDto);
    }


    /**
     * Retrieves all events currently available.
     *
     * @return a response entity containing a list of all event DTOs
     */
    @GetMapping("/all")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        List<EventDto> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    /**
     * Retrieves an event by its ID.
     *
     * @param eventId the UUID of the event to retrieve
     * @return the requested event DTO if found
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(@PathVariable UUID eventId) {
        EventDto event = eventService.getEventById(eventId);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    /**
     * Updates an existing event with the specified ID based on the provided request data.
     *
     * @param eventId the UUID of the event to update
     * @param request the update event request with new event details
     * @return a response entity containing the updated event DTO
     */
    @PatchMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable UUID eventId, @RequestBody UpdateEventRequest request) {
        Event event = modelMapper.map(request, Event.class);
        return new ResponseEntity<>(eventService.updateEvent(eventId, event), HttpStatus.OK);
    }

    /**
     * Updates the images associated with a specific event.
     *
     * This method receives an event ID and an update request object, and it uses these to update the event's images.
     * The {@code @PatchMapping} annotation indicates that this method will respond to HTTP PATCH requests at the specified URI.
     *
     * @param eventId The UUID of the event to update.
     * @param request The request body containing updates for the event, typically including new images.
     * @return A {@link ResponseEntity} containing the updated {@link EventDto} and the HTTP status.
     */
    @PatchMapping("/{eventId}/images")
    public ResponseEntity<EventDto> updateImages(@PathVariable UUID eventId, @RequestBody UpdateEventRequest request) {
        Event event = modelMapper.map(request, Event.class);
        return new ResponseEntity<>(eventService.updateImages(eventId, event), HttpStatus.OK);
    }

    /**
     * Deletes an event by its ID.
     *
     * @param eventId the UUID of the event to delete
     * @return a response entity containing the deleted event DTO
     */
    @DeleteMapping("/{eventId}")
    public ResponseEntity<EventDto> deleteEvent(@PathVariable UUID eventId) {
        EventDto deletedEvent = eventService.deleteEvent(eventId);
        return new ResponseEntity<>(deletedEvent, HttpStatus.OK);
    }

    /**
     * Registers a user to an event by their IDs.
     *
     * @param eventId the UUID of the event
     * @param userId the UUID of the user to register
     * @return a response entity containing the eventRegistration DTO after registration
     */
    @PostMapping("/{eventId}/register/{userId}")
    public ResponseEntity<EventRegistrationDto> registerEvent(@PathVariable UUID eventId, @PathVariable UUID userId) {
        EventRegistration registration = eventRegistrationService.registerUserToEvent(eventId, userId);
        EventRegistrationDto registrationDto = modelMapper.map(registration, EventRegistrationDto.class);
        return new ResponseEntity<>(registrationDto, HttpStatus.OK);
    }

    /**
     * Unregisters a user from an event by their IDs.
     *
     * @param eventId the UUID of the event
     * @param userId the UUID of the user to unregister
     * @return a response entity containing the eventRegistration DTO after unregistration
     */
    @DeleteMapping("/{eventId}/remove/{userId}")
    public ResponseEntity<EventRegistrationDto> unregisterEvent(@PathVariable UUID eventId, @PathVariable UUID userId) {
        EventRegistration registration = eventRegistrationService.removeUserToEvent(eventId, userId);
        EventRegistrationDto registrationDto = modelMapper.map(registration, EventRegistrationDto.class);
        return new ResponseEntity<>(registrationDto, HttpStatus.OK);
    }

    /**
     * Retrieves a list of participant UUIDs for a given event.
     * This method responds to a GET request at the path '/{eventId}/participants'.
     * It calls the service layer to fetch all participants registered for the specified event.
     *
     * @param eventId The unique identifier of the event for which participants are being queried.
     * @return A list of UUIDs representing the participants of the event. The list is empty if no participants are found.
     */
    @GetMapping("/{eventId}/participants")
    public List<UUID> getParticipantsByEventId(@PathVariable UUID eventId) {
        List<UUID> users = eventRegistrationService.getParticipantsByEventId(eventId);
        return users;
    }

    /**
     * Retrieves a list of all events that a specific user has registered for.
     *
     * @param userId The UUID of the user whose registered events are to be retrieved.
     * @return A ResponseEntity containing a list of EventDto objects for the registered events.
     */
    @GetMapping("/by-userId")
    public ResponseEntity<List<EventDto>> getEventsByUserId(@RequestParam UUID userId) {
        List<EventDto> events = eventRegistrationService.getEventsByUserId(userId);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    /**
     * A simple test endpoint to check if the service is running.
     *
     * @return a string "test" indicating the service is operational
     */
    @GetMapping("/test")
    public String test() {
        return "testss";
    }

    @PostMapping("/rating")
    public Rating rating(@Valid @RequestBody Rating rating) {
        return eventService.rateEvent(rating);
    }

    @GetMapping("/rating/{eventId}")
    public List<Rating> getRatingByEventId(@PathVariable UUID eventId) {
        return eventService.getRatingsByEventId(eventId);
    }

    @GetMapping("/rating")
    public List<Rating> getAllRatings() {
        return eventService.getAllRatings();
    }
}
