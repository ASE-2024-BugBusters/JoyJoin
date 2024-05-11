package com.joyjoin.postservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikePostRequest {
    private UUID postId;
    private UUID likeUsersId;
    private Boolean liked;
}
