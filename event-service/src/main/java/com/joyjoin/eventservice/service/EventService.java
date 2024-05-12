package com.joyjoin.eventservice.service;

import com.joyjoin.eventservice.exception.EventNotExpiredException;
import com.joyjoin.eventservice.exception.ResourceNotFoundException;
import com.joyjoin.eventservice.model.*;
import com.joyjoin.eventservice.modelDto.EventDto;
import com.joyjoin.eventservice.packer.EventPacker;
import com.joyjoin.eventservice.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides comprehensive services for managing events, including creation, update,
 * deletion, and retrieval of event information, alongside image management for events.
 */
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final EventParticipationCountRepository eventParticipationCountRepository;
    private final EventRatingRepository eventRatingRepository;
    private final ImageService imageService;
    private final EventPacker eventPacker;
    private final ModelMapper modelMapper;
    private final Environment env;

    /**
     * Constructs an EventService with the necessary components for event management.
     *
     * @param eventRepository Repository interface for basic CRUD operations on events.
     * @param eventRegistrationRepository Repository for managing registrations associated with events.
     * @param eventParticipationCountRepository Repository for tracking the number of participants per event.
     * @param imageService Service dedicated to handling operations related to event images, such as generating upload URLs.
     * @param eventPacker Utility that converts between Event entities and their corresponding Data Transfer Objects (DTOs).
     * @param modelMapper Tool for mapping between domain and DTO objects.
     * @param env Provides environment-specific configuration (e.g., variables from application properties).
     */
    @Autowired
    public EventService(EventRepository eventRepository, EventRegistrationRepository eventRegistrationRepository, EventParticipationCountRepository eventParticipationCountRepository, EventRatingRepository eventRatingRepository, ImageService imageService, EventPacker eventPacker, ModelMapper modelMapper, Environment env) {
        this.eventRepository = eventRepository;
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.eventParticipationCountRepository = eventParticipationCountRepository;
        this.eventRatingRepository = eventRatingRepository;
        this.imageService = imageService;
        this.eventPacker = eventPacker;
        this.modelMapper = modelMapper;
        this.env = env;
    }

    /**
     * Generates upload information for an image, including a pre-signed URL and expiration time.
     *
     * @param expireTime The expiration time for the upload URL.
     * @return Image object containing the bucket name, key, and a single image upload URL.
     */
    public Image getImgUploadInformation(LocalDateTime expireTime) {
        String key = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        String uploadUrl = imageService.getPreSignedUrlForUpload(env.getProperty("s3.BUCKET_NAME"), key, Duration.between(now, expireTime));
        ImageUrl imageUploadUrl = new ImageUrl(uploadUrl, expireTime);
        return new Image(env.getProperty("s3.BUCKET_NAME"), key, List.of(imageUploadUrl));
    }

    /**
     * Saves a new event or updates an existing one in the database, including the registration of initial participation data.
     *
     * @param event Event object containing the details to be saved or updated.
     * @return EventDto containing the saved or updated event data.
     */
    @Transactional
    public EventDto saveEvent(Event event) {
        Event savedEvent = eventRepository.save(event);
        EventParticipationCount count = new EventParticipationCount(savedEvent.getEventId(), 0, true);
        eventParticipationCountRepository.save(count);
        return eventPacker.packEvent(savedEvent);
    }

    /**
     * Retrieves a list of all events that have not been marked as deleted or expired.
     *
     * @return List of EventDto representing all active events.
     */
    @Transactional
    public List<EventDto> getAllEvents() {
        List<Event> events = eventRepository.findByIsDeletedFalseAndIsExpiredFalse();
        return events.stream().map(eventPacker::packEvent).collect(Collectors.toList());
    }

    /**
     * Retrieves a list of events filtered by specified criteria such as title, city, date, and tags.
     * Optionally excludes events that are fully booked.
     *
     * @param title Optional filter for event titles.
     * @param city Optional filter for city location of the events.
     * @param date Optional filter for the event date.
     * @param tags Optional list of tags to filter the events.
     * @param excludeFullEvents Boolean indicating whether to exclude events that have reached their participation limit.
     * @return List of EventDto that match the given filters.
     */
    public List<EventDto> getFilteredEvents(String title, String city, LocalDate date, List<String> tags, boolean excludeFullEvents) {
        List<Specification<Event>> specs = new ArrayList<>();
        specs.add(EventSpecifications.hasTitle(title));
        specs.add(EventSpecifications.isInCity(city));
        specs.add(EventSpecifications.isAtDate(date));
        specs.add(EventSpecifications.hasTags(tags));
        if (excludeFullEvents) {
            specs.add(EventSpecifications.participationLimitNotReached());
        }

        Specification<Event> combinedSpec = EventSpecifications.combine(specs.toArray(new Specification[0]));
        List<Event> events = eventRepository.findAll(combinedSpec);
        return events.stream().map(eventPacker::packEvent).collect(Collectors.toList());
    }

    /**
     * Retrieves an event by its UUID if it has not been marked as deleted or expired.
     *
     * @param eventId The UUID of the event to retrieve.
     * @return EventDto representing the event details.
     * @throws ResourceNotFoundException if the event does not exist or has been deleted.
     */
    @Transactional
    public EventDto getEventById(UUID eventId) {
        var event = eventRepository.findByEventIdAndIsDeletedFalse(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId.toString(),
                        Collections.singletonList("This event may have been deleted or does not exist.")));
        return eventPacker.packEvent(event);
    }

    /**
     * Updates an existing event's details based on the provided new details except for its images.
     *
     * @param eventId The UUID of the event to update.
     * @param eventDetails Event object containing the new details to be updated.
     * @return EventDto containing the updated event data.
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
     * Updates the images for an existing event based on the provided new image details.
     *
     * @param eventId The UUID of the event to update.
     * @param eventDetails Event object containing the new image details.
     * @return EventDto containing the updated event data.
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
     * Marks an event as deleted based on its UUID. Also marks all associated registrations as inactive.
     *
     * @param eventId The UUID of the event to delete.
     * @return EventDto of the deleted event.
     * @throws ResourceNotFoundException if the event does not exist or has already been deleted.
     */
    @Transactional
    public EventDto deleteEvent(UUID eventId) {
        Event event = eventRepository.findByEventIdAndIsDeletedFalseAndIsExpiredFalse(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId.toString(),
                        Collections.singletonList("This event may have been deleted or does not exist.")));
        List<EventRegistration> eventRegistrations = eventRegistrationRepository.findByEventIdAndIsDeletedFalse(eventId);
        for (EventRegistration registration : eventRegistrations) {
            registration.setDeleted(false);
        }
        eventRegistrationRepository.saveAll(eventRegistrations);

        Optional<EventParticipationCount> eventParticipationCount = eventParticipationCountRepository.findByEventId(eventId);
        eventParticipationCount.ifPresent(count -> {
            count.setActive(false);
            eventParticipationCountRepository.save(count);
        });

        event.setDeleted(true);
        eventRepository.save(event);
        return eventPacker.packEvent(event);
    }

    public Rating rateEvent(Rating rating) {
        Event event = eventRepository.findById(rating.getEventId()).orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", rating.getEventId().toString(),
                Collections.singletonList("This event may have been deleted or does not exist.")));
        if (event.isExpired()) {
            return eventRatingRepository.save(rating);
        } else {
            throw new EventNotExpiredException("The event did not happened yet, therefor it can't be rated.");
        }
    }

    public List<Rating> getRatingsByEventId(UUID eventId) {
        return eventRatingRepository.findRatingByEventId(eventId);
    }

    public List<Rating> getAllRatings() {
        return eventRatingRepository.findAll();
    }

    public List<Event> getAttendedEvents(UUID userId) {
        return eventRepository.findByCreatorIdAndIsDeletedFalseAndIsExpiredTrue(userId);
    }
}
