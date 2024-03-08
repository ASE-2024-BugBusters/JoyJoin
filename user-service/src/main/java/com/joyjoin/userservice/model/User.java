package com.joyjoin.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import com.joyjoin.userservice.model.template.DefaultProperties;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "_user")
@Entity
public class User extends DefaultProperties{
    @Id
    @GeneratedValue
    private UUID id;

    private LocalDateTime createdOn = LocalDateTime.now();

    @Setter
    private LocalDateTime lastEdited = LocalDateTime.now();

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
    private String accountName;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    private boolean loggedIn = false;
    private boolean deactivated = false;
    private String postTag;


    /**
     *     Use this to add new columns with default values to avoid breaking the actual DB
     *     @Column(columnDefinition = "boolean DEFAULT false")
     *     private boolean test = false;
     */

}
