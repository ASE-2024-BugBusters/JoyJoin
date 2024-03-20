package com.joyjoin.userservice.model;

import com.joyjoin.userservice.model.converter.ImageRefConverter;
import com.joyjoin.userservice.model.converter.TagsConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import com.joyjoin.userservice.model.template.DefaultProperties;


import java.util.Date;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "_user")
@Entity

public class User extends DefaultProperties {
    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    @NotBlank(message = "Email can't be empty")
    @Email(message = "Invalid Email format")
    private String email;

    @NotBlank(message = "Password can't be empty")
    private String password;

    @Column(unique = true)
    private String userName;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    private String nickname;

    private String biography;

    @Convert(converter = TagsConverter.class)
    private List<InterestTag> interestTags;

    @Convert(converter = ImageRefConverter.class)
    private ImageRef avatar;

    private ProfileVisibility profileVisibility;

    private boolean loggedIn = false;
    private boolean deactivated = false;
    private String tag;


    /**
     *     Use this to add new columns with default values to avoid breaking the actual DB
     *     @Column(columnDefinition = "boolean DEFAULT false")
     *     private boolean test = false;
     */

}
