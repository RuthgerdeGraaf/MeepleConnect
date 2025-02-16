package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.exceptions.GameNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionControllerTest {

    private ExceptionController exceptionController;

    @BeforeEach
    void setUp() {
        exceptionController = new ExceptionController();
    }

    @Test
    void handleGameNotFoundException_ShouldReturn404() {
        GameNotFoundException exception = new GameNotFoundException("Bordspel niet gevonden");

        ResponseEntity<Object> response = exceptionController.handleGameNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Bordspel niet gevonden"));
    }

    @Test
    void handleGlobalException_ShouldReturn500() {
        Exception exception = new Exception("Onverwachte fout");

        ResponseEntity<Object> response = exceptionController.handleGlobalException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Onverwachte fout"));
    }
}
