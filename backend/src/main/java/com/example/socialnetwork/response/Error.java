package com.example.socialnetwork.response;

import org.springframework.http.HttpStatus;

/**
 * Enumeration of errors to be used in responses.
 */
public enum Error {
    AUTHENTICATION_ERROR(HttpStatus.FORBIDDEN, "An authentication error has occurred"),
    REQUIRED_FIELDS(HttpStatus.BAD_REQUEST, "Required fields are missing"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    ALREADY_FOLLOWING(HttpStatus.BAD_REQUEST, "Already following this user"),
    NOT_FOLLOWING(HttpStatus.BAD_REQUEST, "Not following this user");

    private final HttpStatus status;
    private final String reason;

    Error(HttpStatus status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }
}
