package com.core.backend.exception;

public class TokenExpiredException  extends  Exception{
    public TokenExpiredException() {}
    public TokenExpiredException(String message) {
        super(message);
    }
}
