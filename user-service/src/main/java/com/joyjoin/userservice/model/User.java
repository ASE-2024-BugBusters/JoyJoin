package com.joyjoin.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import com.joyjoin.userservice.model.template.DefaultProperties;
import org.hibernate.mapping.List;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "_user")
@Entity
public class User extends DefaultProperties{

    @Setter
    private boolean isDeleted = false;
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

    private boolean loggedIn = false;
    private boolean deactivated = false;
    private String tag;


    /**
     *     Use this to add new columns with default values to avoid breaking the actual DB
     *     @Column(columnDefinition = "boolean DEFAULT false")
     *     private boolean test = false;
     */

}
