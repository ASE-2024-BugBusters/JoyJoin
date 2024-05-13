package com.joyjoin.eventservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "event_participation_count")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class EventParticipationCount {
    @Id
    private UUID eventId;
    private Integer participantCount = 0;

    // when event is expired or deleted, the eventRegistrationCount instance will be marked as isActive = false;
    private boolean isActive = true;
}
