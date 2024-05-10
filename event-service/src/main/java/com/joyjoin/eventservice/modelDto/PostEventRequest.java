package com.joyjoin.eventservice.modelDto;

import com.joyjoin.eventservice.model.ImageRef;
import com.joyjoin.eventservice.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostEventRequest {
    @NotBlank(message = "Title is required")
    private String title;
    @NotNull(message = "Time is required")
    @Future(message = "Time must be in the future")
    private LocalDateTime time;
    @NotNull(message = "Location is required")
    private LocationDto location;
    @Min(value = 2, message = "Participation limit must be at least 1")
    @NotNull(message = "Participation Limit is required")
    private Integer participationLimit;
    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;
    private List<@NotBlank(message = "Tags must not be blank") Tag> tags;
    private List<ImageRef> images;
    private UUID creatorId;
}
