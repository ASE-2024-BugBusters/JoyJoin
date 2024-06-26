package com.joyjoin.postservice.modelDto.Event;

import com.joyjoin.postservice.model.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class EventDto {
    private UUID eventId;
    private String title;
    private LocalDateTime time;
    private LocationDto location;
    private Integer participationLimit;
    private String description;
    private List<String> tags;
    private List<Image> images;
    private LocalDateTime createdOn;
    private LocalDateTime lastEdited;
    private UUID creatorId;
    private List<UUID> participants;
}
