package com.joyjoin.postservice.modelDto;

import com.joyjoin.postservice.modelDto.User.UserDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentWithUserInfoDto {
    private UUID id;
    private UserDto user;
    private UUID postId;
    private String comment;
    private LocalDateTime createdOn;

}
