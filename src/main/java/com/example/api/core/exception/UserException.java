package com.example.api.core.exception;

import org.springframework.http.HttpStatus;

public class UserException extends InternalArchitectureException {

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getErrorCode() {
        return HttpStatus.BAD_REQUEST;
    }

}
