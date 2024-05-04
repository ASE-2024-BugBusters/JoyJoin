package com.joyjoin.postservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostCommentRequest {
    private UUID userId;
    private UUID postId;
    @NotBlank(message = "Comment is required")
    @Size(max = 1000, message = "Comment must be less than 1000 characters")
    private String comment;
}
