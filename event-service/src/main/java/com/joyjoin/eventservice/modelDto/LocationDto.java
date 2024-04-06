package com.joyjoin.eventservice.modelDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class LocationDto {
    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "Number is required")
    private Integer number;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @NotBlank(message = "Country is required")
    private String country;
}
