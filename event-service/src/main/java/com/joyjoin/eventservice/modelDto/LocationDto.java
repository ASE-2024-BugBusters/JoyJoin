package com.joyjoin.eventservice.modelDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    @NotBlank(message = "Street is required")
    private String street;
    @NotBlank(message = "Number is required")
    private Integer number;
    @NotBlank(message = "City is required")
    private String city;
    private Integer postalCode;
}
