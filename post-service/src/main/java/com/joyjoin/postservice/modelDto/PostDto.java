package com.joyjoin.postservice.modelDto;

import com.joyjoin.postservice.model.Image;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostDto {
    private UUID id;
    private UUID userId;
    private String caption;
    private List<UUID> taggedUsersId;
    private UUID taggedEventId;
    private List<UUID> likedUsersId;
    private List<Image> images;
    private LocalDateTime createdOn;
}
