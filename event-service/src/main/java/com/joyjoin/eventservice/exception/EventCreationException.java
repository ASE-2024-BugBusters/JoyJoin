package com.joyjoin.eventservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EventCreationException extends RuntimeException {
    public EventCreationException(String message) {
        super(message);
    }
}
