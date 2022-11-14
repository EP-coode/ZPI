package com.core.backend.exception;

public class NoUserException extends Exception{

    public NoUserException() {}
    public NoUserException(String message) {
        super(message);
    }
}
