package com.core.backend.exception;

public class NoVerificationTokenException extends Exception{
    public NoVerificationTokenException() {}
    public NoVerificationTokenException(String message) {
        super(message);
    }
}
