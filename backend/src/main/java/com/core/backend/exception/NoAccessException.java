package com.core.backend.exception;

public class NoAccessException extends Exception{

    public NoAccessException() {}
    public NoAccessException(String message) {
        super(message);
    }
}