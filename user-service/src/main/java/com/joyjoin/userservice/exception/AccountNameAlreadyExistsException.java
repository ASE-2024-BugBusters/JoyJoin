package com.joyjoin.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AccountNameAlreadyExistsException extends RuntimeException {
    public AccountNameAlreadyExistsException(String message) {
        super(message);
    }
}
