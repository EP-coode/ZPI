package com.core.backend.exception;

public class WrongIdException extends Exception {
    public WrongIdException() {
    }
    public WrongIdException(String message) {
        super(message);
    }
}
