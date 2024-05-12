package com.joyjoin.eventservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EventNotExpiredException extends RuntimeException {
    public EventNotExpiredException(String message) {
        super(message);
    }
}
