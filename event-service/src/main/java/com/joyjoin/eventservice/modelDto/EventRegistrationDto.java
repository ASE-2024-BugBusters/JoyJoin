package com.joyjoin.eventservice.modelDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRegistrationDto {
    private UUID registrationId;
    private UUID eventId;
    private UUID userId;
}
