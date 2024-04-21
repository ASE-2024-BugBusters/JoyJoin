package com.joyjoin.eventservice.repository;

import com.joyjoin.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    // Custom database queries can be defined here if needed
    List<Event> findByIsDeletedFalse();
    Optional<Event> findByIdAndIsDeletedFalse(UUID id);

    Event findEventByTitle(String title);
}