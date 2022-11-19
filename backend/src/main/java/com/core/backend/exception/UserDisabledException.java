package com.core.backend.exception;

public class UserDisabledException extends Exception{
    public UserDisabledException() {}
    public UserDisabledException(String message) {
        super(message);
    }
}
