package com.joyjoin.eventservice.service;
import com.joyjoin.eventservice.exception.InvalidRegisterOrUnregisterToEventException;
import com.joyjoin.eventservice.exception.ResourceNotFoundException;
import com.joyjoin.eventservice.model.Image;
import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.model.ImageRef;
import com.joyjoin.eventservice.model.ImageUrl;
import com.joyjoin.eventservice.modelDto.EventDto;
import com.joyjoin.eventservice.packer.EventPacker;
import com.joyjoin.eventservice.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ImageService imageService;
    private final EventPacker eventPacker;
    private final ModelMapper modelMapper;
    static final String EVENT_BUCKET = "img";

    @Autowired
    public EventService(EventRepository eventRepository, ImageService imageService, EventPacker eventPacker, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.imageService = imageService;
        this.eventPacker = eventPacker;
        this.modelMapper = modelMapper;
    }

    public Image getImgUploadInformation(LocalDateTime expireTime) {
        String key = String.valueOf(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();
        String uploadUrl = imageService.getPreSignedUrlForUpload(EVENT_BUCKET, key, Duration.between(now, expireTime));
        ImageUrl imageUploadUrl = new ImageUrl(uploadUrl, expireTime);
        return new Image(EVENT_BUCKET, key, List.of(imageUploadUrl));
    }
    @Transactional
    public EventDto saveEvent(Event event) {
        Event savedEvent = eventRepository.save(event);
        return eventPacker.packEvent(savedEvent);
    }
    @Transactional
    public List<EventDto> getAllEvents() {
        List<Event> events = eventRepository.findByIsDeletedFalse();
        return events.stream().map(eventPacker::packEvent).collect(Collectors.toList());
    }
    @Transactional
    public EventDto getEventById(UUID eventId) {
        var event = eventRepository.findByEventIdAndIsDeletedFalse(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId.toString(),
                        Collections.singletonList("This event may have been deleted or does not exist.")));
        return eventPacker.packEvent(event);
    }
    @Transactional
    public EventDto updateEvent(UUID eventId, Event eventDetails) {
        var existedEvent = eventRepository.findByEventIdAndIsDeletedFalse(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId.toString(),
                        Collections.singletonList("This event may have been deleted or does not exist.")));
        if (eventDetails.getTags() != null) {
            existedEvent.getTags().clear();
        }
        modelMapper.map(eventDetails, existedEvent);
        Event savedEvent = eventRepository.save(existedEvent);
        return eventPacker.packEvent(savedEvent);
    }
    @Transactional
    public EventDto deleteEvent(UUID eventId) {
        Event event = eventRepository.findByEventIdAndIsDeletedFalse(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId.toString(),
                        Collections.singletonList("This event may have been deleted or does not exist.")));
        event.setDeleted(true);
        eventRepository.save(event);
        return eventPacker.packEvent(event);
    }
//    /**
//     * Deletes specific images from an event, assuming the event and images exist.
//     *
//     * @param eventId The UUID of the event.
//     * @param imageKeys A list of keys representing the images to be deleted.
//     */
//    @Transactional
//    public EventDto deleteEventImages(UUID eventId, List<String> imageKeys) {
//        Event event = eventRepository.findByEventIdAndIsDeletedFalse(eventId)
//                .orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId.toString(),
//                        Collections.singletonList("This event may have been deleted or does not exist.")));
//
//        // Determine which images are to be kept
//        List<ImageRef> remainingImages = event.getImages().stream()
//                .filter(imageRef -> !imageKeys.contains(imageRef.getKey()))
//                .collect(Collectors.toList());
//
////        // Remove the images from storage
////        imageKeys.forEach(imageStorageService::deleteImage);
//
//        // Set the remaining images back to the event and save
//        event.setImages(remainingImages);
//        eventRepository.save(event);
//        return eventPacker.packEvent(event);
//    }
}
