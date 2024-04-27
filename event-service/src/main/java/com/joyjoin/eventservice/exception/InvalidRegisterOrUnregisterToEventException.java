package com.joyjoin.eventservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidRegisterOrUnregisterToEventException extends RuntimeException {

    private String resourceName;
    private String fieldName;
    private String fieldValue;
    @Getter @Setter
    private List<String> details;

    public InvalidRegisterOrUnregisterToEventException(String resourceName, String fieldName, String fieldValue, List<String> details) {
        super(String.format("Invalid register or unregister to %s with %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.details = details;
    }
}
