package com.joyjoin.userservice.model;

import com.joyjoin.userservice.model.converter.ImageRefConverter;
import com.joyjoin.userservice.model.converter.TagsConverter;
import com.joyjoin.userservice.security.model.Role;
import com.joyjoin.userservice.security.model.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "_user")
@Entity
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull(message = "The Firstname can't be null")
    @Size(min = 2, message = "The Firstname has to be a least 2 characters long")
    private String firstName;

    @NotNull(message = "The Lastname can't be null")
    @Size(min = 2, message = "The Lastname has to be a least 2 characters long")
    private String lastName;

    @Column(unique = true)
    @NotNull(message = "Email can't be null")
    @Email(message = "Invalid Email format")
    private String email;

    @NotNull(message = "Password can't be empty")
    @Size(min = 8, max = 16, message = "The password must be min 8 characters and max 16 long")
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

    @Setter
    private LocalDateTime createdOn;

    @Setter
    private LocalDateTime lastEdited;

    @Setter
    private boolean isDeleted;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<Role> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName().name()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    /**
     *     Use this to add new columns with default values to avoid breaking the actual DB
     *     @Column(columnDefinition = "boolean DEFAULT false")
     *     private boolean test = false;
     */

}
