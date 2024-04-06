package com.joyjoin.eventservice.modelDto;

import com.joyjoin.eventservice.model.Image;
import com.joyjoin.eventservice.model.ImageRef;
import com.joyjoin.eventservice.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    private String title;
    private String time;
    private LocationDto location;
    private Integer participationLimit;
    private String description;
    private List<Tag> tags;
    private List<ImageRef> images;
}
