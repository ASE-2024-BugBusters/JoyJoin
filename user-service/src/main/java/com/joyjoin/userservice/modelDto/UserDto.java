package com.joyjoin.userservice.modelDto;

import com.joyjoin.userservice.model.template.DefaultProperties;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
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
    private boolean loggedIn;
    private boolean deactivated;
    private LocalDateTime createdOn;
    private boolean isDeleted;
    private LocalDateTime lastEdited;
}
