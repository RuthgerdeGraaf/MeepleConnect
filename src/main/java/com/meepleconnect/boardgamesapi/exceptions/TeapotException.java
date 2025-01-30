package com.meepleconnect.boardgamesapi.exceptions;

public class TeapotException extends RuntimeException {
    public TeapotException() {
        super("I'm just a teapot and therefore cannot play games.");
    }

    public TeapotException(String message) {
        super(message);
    }
}

