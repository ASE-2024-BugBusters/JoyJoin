package com.joyjoin.userservice.security.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;
@Data
@Entity
public class Role {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    public Role(RoleEnum name){
        this.name = name;
    }
    public Role() {}

}

