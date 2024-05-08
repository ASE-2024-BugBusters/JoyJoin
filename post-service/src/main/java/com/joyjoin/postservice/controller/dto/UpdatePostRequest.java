package com.joyjoin.postservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostRequest {
    private String caption;
    private List<UUID> taggedUsersId;
    private UUID taggedEventId;
    private UUID postId;
}
