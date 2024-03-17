package com.joyjoin.userservice.dto;

import com.joyjoin.userservice.model.InterestTag;
import com.joyjoin.userservice.model.ProfileVisibility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String accountName;
    private String nickname;
    private String biography;
    private List<InterestTag> interestTags;
    private ProfileVisibility profileVisibility;
}
