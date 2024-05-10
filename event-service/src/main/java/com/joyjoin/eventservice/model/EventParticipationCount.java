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
    private boolean isActive = true;
}
