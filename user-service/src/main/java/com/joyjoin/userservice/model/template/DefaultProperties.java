package com.joyjoin.userservice.model.template;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
public class DefaultProperties {

    @Id
    @GeneratedValue
    private UUID id;

    @Setter
    private LocalDateTime createdOn;

    @Setter
    private LocalDateTime lastEdited;

    @Setter
    private boolean isDeleted;
}