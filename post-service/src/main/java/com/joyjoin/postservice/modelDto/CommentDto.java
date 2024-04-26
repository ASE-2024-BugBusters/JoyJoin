package com.joyjoin.postservice.modelDto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentDto {
    private UUID id;
    private UUID userId;
    private UUID postId;
    private String comment;
    private LocalDateTime createdOn;
}
