package com.joyjoin.eventservice.model;

import com.joyjoin.eventservice.model.template.DefaultProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

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

    private int participationLimit;
    @Column(length = 1000)
    private String description;
    @ElementCollection
    private Set<String> tags;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = true)
    private byte[] image;
    @Column(nullable = true)
    private String imageContentType; // Stores the MIME type of the image

}
