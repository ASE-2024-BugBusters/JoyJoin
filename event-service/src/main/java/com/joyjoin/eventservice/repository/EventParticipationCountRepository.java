package com.joyjoin.eventservice.repository;

import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.model.EventParticipationCount;
import com.joyjoin.eventservice.model.EventRegistration;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventParticipationCountRepository extends CrudRepository<EventParticipationCount, UUID> {
    Optional<EventParticipationCount> findByEventId(UUID eventId);
    @Modifying
    @Transactional
    @Query("UPDATE EventParticipationCount e SET e.participantCount = e.participantCount + 1 WHERE e.eventId = ?1")
    void incrementCount(UUID eventId);

    @Modifying
    @Transactional
    @Query("UPDATE EventParticipationCount e SET e.participantCount = GREATEST(0, e.participantCount - 1) WHERE e.eventId = ?1")
    void decrementCount(UUID eventId);
}
