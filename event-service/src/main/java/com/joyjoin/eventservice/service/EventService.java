package com.joyjoin.eventservice.service;

import com.joyjoin.eventservice.exception.ResourceNotFoundException;
import com.joyjoin.eventservice.model.Image;
import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.model.ImageUrl;
import com.joyjoin.eventservice.modelDto.EventDto;
import com.joyjoin.eventservice.packer.EventPacker;
import com.joyjoin.eventservice.repository.EventRepository;
import com.joyjoin.eventservice.repository.EventSpecifications;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service layer for managing events.
 * Provides operations to manage events including creating, updating, deleting, and querying event data.
 */
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ImageService imageService;
    private final EventPacker eventPacker;
    private final ModelMapper modelMapper;
    static final String EVENT_BUCKET = "img";

    /**
     * Constructs an EventService with necessary dependencies.
     *
     * @param eventRepository Repository for event data access.
     * @param imageService Service for handling image-related operations.
     * @param eventPacker Utility to convert between Event entities and DTOs.
     * @param modelMapper Utility to map between different object models.
     */
    @Autowired
    public EventService(EventRepository eventRepository, ImageService imageService, EventPacker eventPacker, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.imageService = imageService;
        this.eventPacker = eventPacker;
        this.modelMapper = modelMapper;
    }

    /**
     * Generates upload information for an image, including a pre-signed URL and expiration time.
     *
     * @param expireTime The expiration time for the upload URL.
     * @return An Image object containing the bucket name, key, and a single image upload URL.
     */
    public Image getImgUploadInformation(LocalDateTime expireTime) {
        String key = String.valueOf(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();
        String uploadUrl = imageService.getPreSignedUrlForUpload(EVENT_BUCKET, key, Duration.between(now, expireTime));
        ImageUrl imageUploadUrl = new ImageUrl(uploadUrl, expireTime);
        return new Image(EVENT_BUCKET, key, List.of(imageUploadUrl));
    }

    /**
     * Saves a new event or updates an existing one in the database.
     *
     * @param event Event object containing the details to be saved.
     * @return The saved or updated event data as a DTO.
     */
    @Transactional
    public EventDto saveEvent(Event event) {
        Event savedEvent = eventRepository.save(event);
        return eventPacker.packEvent(savedEvent);
    }

    /**
     * Retrieves all events that have not been marked as deleted.
     *
     * @return A list of Event DTOs.
     */
    @Transactional
    public List<EventDto> getAllEvents() {
        List<Event> events = eventRepository.findByIsDeletedFalseAndIsExpiredFalse();
        return events.stream().map(eventPacker::packEvent).collect(Collectors.toList());
    }

    public List<EventDto> getFilteredEvents(String title, String city, LocalDateTime time, List<String> tags) {
        Specification<Event> spec = EventSpecifications.combine(
                EventSpecifications.hasTitle(title),
                EventSpecifications.isInCity(city),
                EventSpecifications.isAtTime(time),
                EventSpecifications.hasTags(tags)
        );
        List<Event> events = eventRepository.findAll(spec);
        return events.stream().map(eventPacker::packEvent).collect(Collectors.toList());
    }
    /**
     * Retrieves an event by its UUID if it has not been marked as deleted.
     *
     * @param eventId The UUID of the event.
     * @return The Event DTO.
     * @throws ResourceNotFoundException if the event does not exist or has been deleted.
     */
    @Transactional
    public EventDto getEventById(UUID eventId) {
        var event = eventRepository.findByEventIdAndIsDeletedFalseAndIsExpiredFalse(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId.toString(),
                        Collections.singletonList("This event may have been deleted or does not exist.")));
        return eventPacker.packEvent(event);
    }

    /**
     * Updates an existing event's details except for its images.
     *
     * @param eventId The UUID of the event to update.
     * @param eventDetails Event object containing the new details.
     * @return The updated Event DTO.
     * @throws ResourceNotFoundException if the event does not exist or has been deleted.
     */
    @Transactional
    public EventDto updateEvent(UUID eventId, Event eventDetails) {
        var existedEvent = eventRepository.findByEventIdAndIsDeletedFalseAndIsExpiredFalse(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId.toString(),
                        Collections.singletonList("This event may have been deleted or does not exist.")));
        modelMapper.map(eventDetails, existedEvent);
        Event savedEvent = eventRepository.save(existedEvent);
        return eventPacker.packEvent(savedEvent);
    }

    /**
     * Updates the images for an existing event.
     *
     * @param eventId The UUID of the event to update.
     * @param eventDetails Event object containing the new image details.
     * @return The updated Event DTO.
     * @throws ResourceNotFoundException if the event does not exist or has been deleted.
     */
    @Transactional
    public EventDto updateImages(UUID eventId, Event eventDetails) {
        var existedEvent = eventRepository.findByEventIdAndIsDeletedFalseAndIsExpiredFalse(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId.toString(),
                        Collections.singletonList("This event may have been deleted or does not exist.")));
        existedEvent.getImages().clear();
        existedEvent.getImages().addAll(eventDetails.getImages());
        Event savedEvent = eventRepository.save(existedEvent);
        return eventPacker.packEvent(savedEvent);
    }

    /**
     * Marks an event as deleted based on its UUID.
     *
     * @param eventId The UUID of the event to delete.
     * @return The Event DTO of the deleted event.
     * @throws ResourceNotFoundException if the event does not exist or has already been deleted.
     */
    @Transactional
    public EventDto deleteEvent(UUID eventId) {
        Event event = eventRepository.findByEventIdAndIsDeletedFalseAndIsExpiredFalse(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId.toString(),
                        Collections.singletonList("This event may have been deleted or does not exist.")));
        event.setDeleted(true);
        eventRepository.save(event);
        return eventPacker.packEvent(event);
    }
}
