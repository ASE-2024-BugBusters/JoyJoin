package com.joyjoin.userservice.exception;

public enum ErrorMessages {

    USER_NOT_FOUND("User not found"),
    USER_EMAIL_ALREADY_EXISTS("Email already exists, please use a different email address");

    private final String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
