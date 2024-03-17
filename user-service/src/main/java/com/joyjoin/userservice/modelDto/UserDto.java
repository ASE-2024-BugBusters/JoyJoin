package com.joyjoin.userservice.modelDto;


import com.joyjoin.userservice.model.ProfileVisibility;
import com.joyjoin.userservice.model.InterestTag;
import com.joyjoin.userservice.modelDto.userPostDto.PostDto;
import com.joyjoin.userservice.model.template.DefaultProperties;
import lombok.*;


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
    private String avatar;
    private ProfileVisibility profileVisibility;
    private List<PostDto> postDto;
}
