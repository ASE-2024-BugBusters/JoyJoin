package com.joyjoin.eventservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "event_registration")
public class EventRegistration {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, name = "registration_id")
    private UUID registrationId;
    private UUID eventId;
    private UUID userId;

    // when the user is removed from the event or the event is deleted, this registration will be marked as deleted;
    private boolean isDeleted = false;

    // when the event has expired, this registration will be marked as expired;
    private boolean isExpired = false;

}
