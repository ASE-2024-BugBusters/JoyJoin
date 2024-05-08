package com.joyjoin.postservice.modelDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.joyjoin.postservice.model.Comment;
import com.joyjoin.postservice.model.Image;
import com.joyjoin.postservice.model.template.DefaultProperties;
import com.joyjoin.userservice.model.User;
import lombok.*;
import com.joyjoin.userservice.modelDto.UserDto;
import com.joyjoin.eventservice.modelDto.EventDto;

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
//    private List<Image> images;
}
