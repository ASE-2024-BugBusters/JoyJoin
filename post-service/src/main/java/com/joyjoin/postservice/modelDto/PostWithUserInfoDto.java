package com.joyjoin.postservice.modelDto;

import com.joyjoin.postservice.model.Image;
import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostWithUserInfoDto {
    private UUID id;
    private UserDto user;
    private String caption;
    private List<UserDto> taggedUsers;
    private EventDto taggedEvent;
//    private List<UserDto> likedUsers;
    private List<Image> images;
}
