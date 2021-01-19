package com.app.userrestapi.exception;

public class ApiException extends Exception {
    private static final long serialVersionUID = -1255670828930450176L;

    public ApiException(String message, Exception e) {
        super(message, e);
    }
}
