package com.joyjoin.postservice.controller.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikePostRequest {
    private UUID postId;
    private UUID likeUsersId;
    private Boolean liked;
}
