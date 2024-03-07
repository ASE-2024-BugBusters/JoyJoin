package com.joyjoin.userservice.modelDto;

import com.joyjoin.userservice.model.template.DefaultProperties;
import com.joyjoin.userservice.modelDto.postService.PostDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto extends DefaultProperties {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String accountName;
    private boolean loggedIn;
    private boolean deactivated;
    private List<PostDto> postDto;
}
