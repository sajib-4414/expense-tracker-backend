package com.sajib_4414.expense.tracker.config.exceptions;



public class JWTExpiredException extends RuntimeException {
    public JWTExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}