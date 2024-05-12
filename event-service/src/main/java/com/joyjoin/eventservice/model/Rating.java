package com.joyjoin.eventservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Rating {

    @Id
    @GeneratedValue
    private UUID id;
    private UUID userId;
    private UUID eventId;

    @Column(columnDefinition = "TEXT")
    private String comment = "";

    @Min(value = 0, message = "The min rating is 0") @Max(value = 5, message = "the max rating is 5")
    private Double rating;
}
