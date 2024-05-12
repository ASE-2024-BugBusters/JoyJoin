package com.joyjoin.postservice.model;

import com.joyjoin.postservice.model.template.DefaultProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "comment")
@Entity
@Builder
public class Comment extends DefaultProperties {

    private UUID userId;

    private UUID postId;

    private String comment;

}
