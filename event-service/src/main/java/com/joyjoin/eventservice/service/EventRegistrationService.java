package com.joyjoin.eventservice.service;

import com.joyjoin.eventservice.exception.DuplicateRegistrationException;
import com.joyjoin.eventservice.exception.EventRegistrationNotFoundException;
import com.joyjoin.eventservice.model.EventRegistration;
import com.joyjoin.eventservice.modelDto.EventDto;
import com.joyjoin.eventservice.repository.EventRegistrationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventRegistrationService {
    private final EventService eventService;
    private final EventRegistrationRepository eventRegistrationRepository;
    @Autowired
    public EventRegistrationService(EventService eventService, EventRegistrationRepository eventRegistrationRepository) {
        this.eventService = eventService;
        this.eventRegistrationRepository = eventRegistrationRepository;
    }
    @Transactional
    public EventRegistration registerUserToEvent(UUID eventId, UUID userId) {
        eventRegistrationRepository.findByEventIdAndUserIdAndIsRegistered(eventId, userId, true)
                .ifPresent(registration -> {
                    Map<String, String> fields = new HashMap<>();
                    fields.put("eventId", eventId.toString());
                    fields.put("userId", userId.toString());
                    throw new DuplicateRegistrationException("EventRegistration", fields,
                            Collections.singletonList("User is already registered to this event"));
                });
        EventRegistration eventRegistration = new EventRegistration();
        eventRegistration.setEventId(eventId);
        eventRegistration.setUserId(userId);
        return eventRegistrationRepository.save(eventRegistration);
    }
    @Transactional
    public EventRegistration removeUserToEvent(UUID eventId, UUID userId) {
        EventRegistration eventRegistration = eventRegistrationRepository.findByEventIdAndUserIdAndIsRegistered(eventId, userId, true).orElseThrow(() -> {
            Map<String, String> fields = new HashMap<>();
            fields.put("eventId", eventId.toString());
            fields.put("userId", userId.toString());
            return new EventRegistrationNotFoundException("EventRegistration", fields,
                    Collections.singletonList("User is not registered to the event."));
        });
        eventRegistration.setRegistered(false);
        return eventRegistrationRepository.save(eventRegistration);
    }
    @Transactional
    public List<UUID> getParticipantsByEventId(UUID eventId) {
        List<EventRegistration> eventRegistrations = eventRegistrationRepository.findByEventIdAndIsRegistered(eventId, true);
        return eventRegistrations.stream()
                .map(EventRegistration::getUserId)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<EventDto> getEventsByUserId(UUID userId) {
        List<EventRegistration> eventRegistrations = eventRegistrationRepository.findByUserIdAndIsRegistered(userId, true);
        List<UUID> eventIds = eventRegistrations.stream()
                .map(EventRegistration::getEventId)
                .collect(Collectors.toList());
        List<EventDto> eventDtos = eventIds.stream()
                .map(eventService::getEventById)  // Convert each eventId to an EventDto
                .collect(Collectors.toList());

        return eventDtos;
    }
}