package com.joyjoin.eventservice.model;
import com.joyjoin.eventservice.model.converter.ImageRefListConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, name = "event_id")
    private UUID eventId;
    @Setter
    private LocalDateTime createdOn;
    @Setter
    private LocalDateTime lastEdited;
    @Setter
    @Column(name = "is_deleted")
    private boolean isDeleted = false;
    @NotBlank
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
    @Column(length = 10000)
    private List<ImageRef> images;
    @PrePersist
    protected void onCreate() {
        createdOn = LocalDateTime.now();
        lastEdited = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        lastEdited = LocalDateTime.now();
    }
    @Column(name = "creator_id", nullable = false)
    private UUID creatorId;
    @ElementCollection
    private List<UUID> participants;
}
