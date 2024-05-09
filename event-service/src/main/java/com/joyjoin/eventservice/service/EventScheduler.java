package com.joyjoin.eventservice.service;
import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.repository.EventRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventScheduler {
    private final EventRepository eventRepository;
    @Autowired
    public EventScheduler(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Cron expression: "second, minute, hour, day of month, month, day(s) of week"
    @Scheduled(cron = "0 * * * * ?") // Runs at the start of every minute
    public void updateExpiredEvents() {
        LocalDateTime now = LocalDateTime.now();
        List<Event> events = eventRepository.findAll();
        events.forEach(event -> {
            if (event.getTime().isBefore(now) && !event.isExpired()) {
                event.setExpired(true);
            }
        });
        eventRepository.saveAll(events);
    }
}
