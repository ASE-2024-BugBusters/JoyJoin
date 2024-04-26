package com.joyjoin.postservice.controller.dto;

import com.joyjoin.postservice.model.ImageRef;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
    private UUID userId;
    @NotBlank(message = "Caption is required")
    @Size(max = 1000, message = "Caption must be less than 1000 characters")
    private String caption;
    private List<UUID> taggedUsersId;
    private UUID taggedEventId;
    private List<UUID> likedUsersId;
//    private List<ImageRef> images;
}
