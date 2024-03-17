package com.joyjoin.eventservice.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Location {
    private String street;
    private String number;
    private String city;
    private String postalCode;
    private String country;

    // Constructors, getters, setters
    public Location(String street, String number, String city, String postalCode, String country) {
        this.street = street;
        this.number = number;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    // toString method if needed
}
