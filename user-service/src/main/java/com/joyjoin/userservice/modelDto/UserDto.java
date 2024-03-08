package com.joyjoin.userservice.modelDto;


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
public class UserDto extends DefaultProperties {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String accountName;
    private List<PostDto> postDto;
}
