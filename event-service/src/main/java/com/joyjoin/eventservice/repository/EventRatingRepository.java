package com.joyjoin.eventservice.repository;

import com.joyjoin.eventservice.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EventRatingRepository extends JpaRepository<Rating, UUID> {
    @Query("SELECT r FROM Rating r WHERE r.eventId = :eventId")
    List<Rating> findRatingByEventId(UUID eventId);
}
