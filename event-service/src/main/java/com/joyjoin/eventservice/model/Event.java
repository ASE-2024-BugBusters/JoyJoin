package com.joyjoin.eventservice.model;
import com.joyjoin.eventservice.model.converter.ImageRefListConverter;
import com.joyjoin.eventservice.model.converter.TagsConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
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

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private LocalDateTime lastEdited;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired = false;
    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime time;

    @Embedded
    private Location location;

    private Integer participationLimit;

    @NotBlank
    @Size(max = 1000)
    @Column(length = 1000, nullable = false)
    private String description;

    @Convert(converter = TagsConverter.class)
    private List<Tag> tags;

    @Convert(converter = ImageRefListConverter.class)
    @Column(length = 10000)
    private List<ImageRef> images;

    @NotNull
    @Column(name = "creator_id", nullable = false)
    private UUID creatorId;

    @PrePersist
    protected void onCreate() {
        createdOn = LocalDateTime.now();
        lastEdited = LocalDateTime.now();
        checkAndSetExpired();
    }

    @PreUpdate
    protected void onUpdate() {
        lastEdited = LocalDateTime.now();
        checkAndSetExpired();
    }
    private void checkAndSetExpired() {
        isExpired = LocalDateTime.now().isAfter(this.time);
    }
}
