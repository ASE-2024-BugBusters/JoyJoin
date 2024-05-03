package com.joyjoin.eventservice.modelDto;

import com.joyjoin.eventservice.model.Image;
import com.joyjoin.eventservice.model.ImageRef;
import com.joyjoin.eventservice.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    private String title;
    private LocalDateTime time;
    private LocationDto location;
    private Integer participationLimit;
    private String description;
    private List<String> tags;
    private List<ImageRef> images;
}
