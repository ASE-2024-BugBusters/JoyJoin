package com.joyjoin.eventservice.modelDto;

import com.joyjoin.eventservice.model.Image;
import com.joyjoin.eventservice.model.ImageRef;
import com.joyjoin.eventservice.model.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class EventDto {
    private UUID id;
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Time is required")
    @Future(message = "Time must be in the future")
    private LocalDateTime time;

    @NotNull(message = "Location is required")
    private LocationDto location;

    @Min(value = 1, message = "Participation limit must be at least 1")
    private int participationLimit;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    private Set<@NotBlank(message = "Tags must not be blank") String> tags;
    private List<Image> images;
}
