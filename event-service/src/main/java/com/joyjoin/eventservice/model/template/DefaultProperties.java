package com.joyjoin.eventservice.model.template;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
public class DefaultProperties {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Setter
    private LocalDateTime createdOn = LocalDateTime.now();

    @Setter
    private LocalDateTime lastEdited = LocalDateTime.now();

    @Setter
    private boolean isDeleted = false;
}
