package com.joyjoin.userservice.model;

import com.joyjoin.userservice.model.template.DefaultProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "_user")
@Entity
public class User extends DefaultProperties {
    private String firstName;
    private String lastName;

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
