package com.joyjoin.postservice.modelDto;


import com.joyjoin.userservice.model.Image;
import com.joyjoin.userservice.model.InterestTag;
import com.joyjoin.userservice.model.ProfileVisibility;
import com.joyjoin.userservice.modelDto.userPostDto.PostDto;
import com.joyjoin.userservice.security.model.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String accountName;
    private String nickname;
    private String biography;
    private List<InterestTag> interestTags;
    private Image avatar;
    private Collection<Role> roles;
    private ProfileVisibility profileVisibility;
    private List<PostDto> postDto;
    private LocalDateTime createdOn;
    private LocalDateTime lastEdited;
}
