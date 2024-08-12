package com.urbanrides.exceptions;

public class CustomExceptions extends Exception {

    public CustomExceptions(String message) {
        super(message);
    }
    public CustomExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}