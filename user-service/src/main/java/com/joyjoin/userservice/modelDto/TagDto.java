package com.joyjoin.userservice.modelDto;

import com.joyjoin.userservice.model.template.DefaultProperties;
import lombok.*;

import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TagDto extends DefaultProperties {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String accountName;
}
