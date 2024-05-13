package com.joyjoin.eventservice.service;
import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.model.EventRegistration;
import com.joyjoin.eventservice.repository.EventParticipationCountRepository;
import com.joyjoin.eventservice.repository.EventRegistrationRepository;
import com.joyjoin.eventservice.repository.EventRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventScheduler {
    private final EventRepository eventRepository;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final EventParticipationCountRepository eventParticipationCountRepository;
    @Autowired
    public EventScheduler(EventRepository eventRepository, EventRegistrationRepository eventRegistrationRepository, EventParticipationCountRepository eventParticipationCountRepository) {
        this.eventRepository = eventRepository;
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.eventParticipationCountRepository = eventParticipationCountRepository;
    }

    // Cron expression: "second, minute, hour, day of month, month, day(s) of week"
    @Scheduled(cron = "0 * * * * ?") // Runs at the start of every minute
    public void updateExpiredEvents() {
        LocalDateTime now = LocalDateTime.now();
        List<Event> events = eventRepository.findAll();
        events.forEach(event -> {
            if (event.getTime().isBefore(now) && !event.isExpired()) {
                event.setExpired(true);
                // Fetch and update all registrations related to this event
                List<EventRegistration> registrations = eventRegistrationRepository.findByEventIdAndIsDeletedFalse(event.getEventId());
                registrations.forEach(registration -> registration.setExpired(true));
                eventRegistrationRepository.saveAll(registrations);

                // Fetch and update the participation count
                eventParticipationCountRepository.findByEventId(event.getEventId())
                        .ifPresent(count -> {
                            count.setActive(false);
                            eventParticipationCountRepository.save(count);
                        });
            }
        });
        eventRepository.saveAll(events);
    }

}
