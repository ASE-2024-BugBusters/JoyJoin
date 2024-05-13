package com.joyjoin.eventservice.service;

import com.joyjoin.eventservice.exception.DuplicateRegistrationException;
import com.joyjoin.eventservice.exception.EventRegistrationNotFoundException;
import com.joyjoin.eventservice.model.EventRegistration;
import com.joyjoin.eventservice.modelDto.EventDto;
import com.joyjoin.eventservice.modelDto.EventRegistrationDto;
import com.joyjoin.eventservice.repository.EventParticipationCountRepository;
import com.joyjoin.eventservice.repository.EventRegistrationRepository;
import com.joyjoin.eventservice.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventRegistrationService {
    private final EventService eventService;
    private final ModelMapper modelMapper;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final EventParticipationCountRepository participationCountRepository;

    @Autowired
    public EventRegistrationService(EventService eventService, ModelMapper modelMapper, EventRegistrationRepository eventRegistrationRepository, EventParticipationCountRepository participationCountRepository) {
        this.eventService = eventService;
        this.modelMapper = modelMapper;
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.participationCountRepository = participationCountRepository;
    }
    @Transactional
    public EventRegistrationDto registerUserToEvent(UUID eventId, UUID userId) {
        eventRegistrationRepository.findByEventIdAndUserIdAndIsDeletedFalse(eventId, userId)
                .ifPresent(registration -> {
                    Map<String, String> fields = new HashMap<>();
                    fields.put("eventId", eventId.toString());
                    fields.put("userId", userId.toString());
                    throw new DuplicateRegistrationException("EventRegistration", fields,
                            Collections.singletonList("User is already registered to this event"));
                });
        EventRegistration eventRegistration = new EventRegistration();
        eventRegistration.setDeleted(false);
        eventRegistration.setEventId(eventId);
        eventRegistration.setUserId(userId);
        participationCountRepository.incrementCount(eventId);
        eventRegistrationRepository.save(eventRegistration);
        EventRegistrationDto registrationDto = modelMapper.map(eventRegistration, EventRegistrationDto.class);
        return registrationDto;
    }
    @Transactional
    public EventRegistrationDto removeUserToEvent(UUID eventId, UUID userId) {
        EventRegistration eventRegistration = eventRegistrationRepository.findByEventIdAndUserIdAndIsDeletedFalse(eventId, userId).orElseThrow(() -> {
            Map<String, String> fields = new HashMap<>();
            fields.put("eventId", eventId.toString());
            fields.put("userId", userId.toString());
            return new EventRegistrationNotFoundException("EventRegistration", fields,
                    Collections.singletonList("User is not registered to the event."));
        });
        eventRegistration.setDeleted(true);
        participationCountRepository.decrementCount(eventId);
        eventRegistrationRepository.save(eventRegistration);
        EventRegistrationDto registrationDto = modelMapper.map(eventRegistration, EventRegistrationDto.class);
        return registrationDto;
    }
    @Transactional
    public List<UUID> getParticipantsByEventId(UUID eventId) {
        List<EventRegistration> eventRegistrations = eventRegistrationRepository.findByEventIdAndIsDeletedFalse(eventId);
        return eventRegistrations.stream()
                .map(EventRegistration::getUserId)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<EventDto> getValidEventsByUserId(UUID userId) {
        List<EventRegistration> eventRegistrations = eventRegistrationRepository.findByUserIdAndIsDeletedFalseAndIsExpiredFalse(userId);
        List<UUID> eventIds = eventRegistrations.stream()
                .map(EventRegistration::getEventId)
                .collect(Collectors.toList());
        List<EventDto> eventDtos = eventIds.stream()
                .map(eventService::getEventById)  // Convert each eventId to an EventDto
                .collect(Collectors.toList());

        return eventDtos;
    }
    @Transactional
    public List<EventDto> getExpiredEventsByUserId(UUID userId) {
        List<EventRegistration> eventRegistrations = eventRegistrationRepository.findByUserIdAndIsDeletedFalseAndIsExpiredTrue(userId);
        List<UUID> eventIds = eventRegistrations.stream()
                .map(EventRegistration::getEventId)
                .collect(Collectors.toList());
        List<EventDto> eventDtos = eventIds.stream()
                .map(eventService::getEventById)  // Convert each eventId to an EventDto
                .collect(Collectors.toList());

        return eventDtos;
    }
}
