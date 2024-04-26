package com.joyjoin.postservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.joyjoin.postservice.model.template.DefaultProperties;
import jakarta.persistence.*;
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

//    @JsonIgnoreProperties("comments")
//    @ManyToOne()
//    @JoinColumn(name = "post_id", nullable = false)
//    private Post post;
    private UUID postId;

    private String comment;

}
