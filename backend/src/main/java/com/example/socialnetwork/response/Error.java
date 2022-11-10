package com.example.socialnetwork.response;

import org.springframework.http.HttpStatus;

/**
 * Enumeration of errors to be used in responses.
 */
public enum Error {
    RANDOM_ERROR(HttpStatus.BAD_REQUEST, "Random reason");

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
