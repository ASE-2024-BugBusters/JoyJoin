package com.joyjoin.eventservice.repository;

import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.model.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, UUID> {
    Optional<EventRegistration> findByEventIdAndUserIdAndIsDeletedFalse(UUID eventId, UUID userId);
    List<EventRegistration> findByEventIdAndIsDeletedFalse(UUID eventId);
    List<EventRegistration> findByUserIdAndIsDeletedFalseAndIsExpiredFalse(UUID userId);
    List<EventRegistration> findByUserIdAndIsDeletedFalseAndIsExpiredTrue(UUID userId);
}
