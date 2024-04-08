package com.joyjoin.eventservice.controller.dto;
import com.joyjoin.eventservice.model.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.joyjoin.eventservice.model.ImageRef;
import com.joyjoin.eventservice.model.Tag;

import java.time.LocalDateTime;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    private String title;
    private LocalDateTime time;
    private Location location;
    private int participationLimit;
    private String description;
    private Set<Tag> tags;
    private ImageRef img;
}
