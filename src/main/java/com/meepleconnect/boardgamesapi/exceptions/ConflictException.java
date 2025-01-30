package com.meepleconnect.boardgamesapi.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException() {
        super("Conflict detected");
    }

    public ConflictException(String message) {
        super(message);
    }
}

