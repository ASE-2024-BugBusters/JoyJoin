package com.joyjoin.postservice.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.joyjoin.postservice.model.ImageRef;
import com.joyjoin.postservice.model.converter.ImageRefListConverter;
import com.joyjoin.postservice.model.template.DefaultProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "posts")
@Entity
@Builder
public class Post extends DefaultProperties {
    private UUID userId;
    @Column(length = 1000)
    private String caption;
    @ElementCollection
    private List<UUID> taggedUsersId;
    private UUID taggedEventId;
    @ElementCollection
    private List<UUID> likedUsersId;

    @Convert(converter = ImageRefListConverter.class)
    @Column(length = 10000)
    private List<ImageRef> images;


}
