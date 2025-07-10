package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.exceptions.BadRequestException;
import com.meepleconnect.boardgamesapi.exceptions.ConflictException;
import com.meepleconnect.boardgamesapi.exceptions.GameNotFoundException;
import com.meepleconnect.boardgamesapi.exceptions.ReservationNotFoundException;
import com.meepleconnect.boardgamesapi.exceptions.TeapotException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionControllerTest {

    private ExceptionController exceptionController;

    @BeforeEach
    void setUp() {
        exceptionController = new ExceptionController();
    }

    @Test
    void handleGameNotFoundException_ShouldReturn404() {
        GameNotFoundException exception = new GameNotFoundException("Boardgame not found");

        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                .handleGameNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Game Not Found", response.getBody().getError());
        assertEquals("Boardgame not found", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleReservationNotFoundException_ShouldReturn404() {
        ReservationNotFoundException exception = new ReservationNotFoundException("Reservation not found");

        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                .handleReservationNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Reservation Not Found", response.getBody().getError());
        assertEquals("Reservation not found", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleGenericException_ShouldReturn500() {
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                .handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Internal Server Error", response.getBody().getError());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleBadRequestException_ShouldReturn400() {
        BadRequestException exception = new BadRequestException("Bad request error");

        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                .handleBadRequestException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Bad Request", response.getBody().getError());
        assertEquals("Bad request error", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleConflictException_ShouldReturn409() {
        ConflictException exception = new ConflictException("Conflict occurred");

        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                .handleConflictException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Conflict", response.getBody().getError());
        assertEquals("Conflict occurred", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleTeapotException_ShouldReturn418() {
        TeapotException exception = new TeapotException("I'm a teapot");

        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                .handleTeapotException(exception);

        assertEquals(HttpStatus.I_AM_A_TEAPOT, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("I'm a Teapot", response.getBody().getError());
        assertEquals("I'm a teapot", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }
}
