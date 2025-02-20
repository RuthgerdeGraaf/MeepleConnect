package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.exceptions.GameNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionControllerTest {

    private ExceptionController exceptionController;

    @BeforeEach
    void setUp() {
        exceptionController = new ExceptionController();
    }

    @Test
    void handleGameNotFoundException_ShouldReturn404() {
        // Arrange
        GameNotFoundException exception = new GameNotFoundException("Bordspel niet gevonden");

        // Act
        ResponseEntity<Object> response = exceptionController.handleGameNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Statuscode moet 404 zijn");
        assertNotNull(response.getBody(), "Response body mag niet null zijn");

        // Controleer de response body inhoud
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(404, body.get("status"));
        assertEquals("Not Found", body.get("error"));
        assertEquals("Bordspel niet gevonden", body.get("message"));
        assertNotNull(body.get("timestamp"), "Timestamp mag niet null zijn");
    }

    @Test
    void handleGlobalException_ShouldReturn500() {
        // Arrange
        Exception exception = new Exception("Onverwachte fout");

        // Act
        ResponseEntity<Object> response = exceptionController.handleGlobalException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Statuscode moet 500 zijn");
        assertNotNull(response.getBody(), "Response body mag niet null zijn");

        // Controleer de response body inhoud
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(500, body.get("status"));
        assertEquals("Internal Server Error", body.get("error"));
        assertEquals("Onverwachte fout", body.get("message"));
        assertNotNull(body.get("timestamp"), "Timestamp mag niet null zijn");
    }
}
