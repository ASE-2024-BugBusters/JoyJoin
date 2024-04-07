package com.joyjoin.userservice.modelDto;

import com.joyjoin.userservice.model.Image;
import lombok.*;

import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserTagDto{

    private UUID id;
    private String firstName;
    private String lastName;
    private Image avatar;
    private String email;
    private String accountName;
}
