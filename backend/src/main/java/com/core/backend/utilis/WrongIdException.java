package com.core.backend.utilis;

public class WrongIdException extends Exception {
    public WrongIdException() {
    }
    public WrongIdException(String message) {
        super(message);
    }
}
