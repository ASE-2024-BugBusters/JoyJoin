package com.joyjoin.userservice.security.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;
@Data
@Entity
public class Role {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    public Role(String name){
        this.name = name;
    }
    public Role() {}
}
