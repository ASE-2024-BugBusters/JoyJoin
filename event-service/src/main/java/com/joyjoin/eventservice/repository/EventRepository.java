package com.joyjoin.eventservice.repository;

import com.joyjoin.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByIsDeletedFalse();
//    @Query("SELECT e FROM Event e WHERE e.eventId = :eventId AND e.isDeleted = false")
//    Optional<Event> findByEventIdAndIsDeletedFalse(@Param("eventId") UUID eventId);
    Optional<Event> findByEventIdAndIsDeletedFalse(UUID eventId);
}
