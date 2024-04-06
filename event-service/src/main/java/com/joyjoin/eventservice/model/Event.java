package com.joyjoin.eventservice.model;
import com.joyjoin.eventservice.model.converter.ImageRefListConverter;
import com.joyjoin.eventservice.model.template.DefaultProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Event extends DefaultProperties {
    private String title;
    private LocalDateTime time;

    @Embedded
    private Location location;

    private Integer participationLimit;
    @Column(length = 1000)
    private String description;
    @ElementCollection
    private List<Tag> tags;
    @Convert(converter = ImageRefListConverter.class)
    private List<ImageRef> images;

}
