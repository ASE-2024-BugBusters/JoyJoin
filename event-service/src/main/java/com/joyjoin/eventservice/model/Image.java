package com.joyjoin.eventservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Image {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID eventId;
    private String imageUrl;

    // Assuming `key` is the unique identifier for the image, and you want to store a single URL.
    public Image(UUID eventId, String uploadUrl) {
        this.eventId = eventId;
        this.imageUrl = uploadUrl;
        // Initialize other necessary fields here
    }
}
