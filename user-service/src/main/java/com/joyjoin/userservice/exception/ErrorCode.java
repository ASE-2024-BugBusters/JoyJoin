package com.joyjoin.userservice.exception;

public enum ErrorCode {
    USER_NOT_FOUND("USER_NOT_FOUND"),
    USER_EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS");

    private final String errorCode;

    ErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
