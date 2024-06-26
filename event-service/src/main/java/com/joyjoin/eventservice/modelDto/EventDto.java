package com.joyjoin.eventservice.modelDto;
import com.joyjoin.eventservice.model.Image;
import com.joyjoin.eventservice.model.Tag;
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
    private List<Tag> tags;
    private List<Image> images;
    private LocalDateTime createdOn;
    private LocalDateTime lastEdited;
    private UUID creatorId;
    private boolean isExpired;
}
