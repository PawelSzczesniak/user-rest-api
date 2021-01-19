package com.app.userrestapi.exception;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -8887670828930450176L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}