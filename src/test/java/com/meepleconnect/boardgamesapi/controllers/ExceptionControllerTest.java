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
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExceptionControllerTest {

    private ExceptionController exceptionController;

    @BeforeEach
    void setUp() {
        exceptionController = new ExceptionController();
    }

    @Test
    void handleGameNotFoundException_ShouldReturn404() {
        GameNotFoundException exception = new GameNotFoundException("Boardgame not found");

        ResponseEntity<Object> response = exceptionController.handleGameNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().toString().contains("Boardgame not found"));
    }

    @Test
    void handleReservationNotFoundException_ShouldReturn404() {
        ReservationNotFoundException exception = new ReservationNotFoundException("Reservation not found");

        ResponseEntity<Object> response = exceptionController.handleReservationNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().toString().contains("Reservation not found"));
    }

    @Test
    void handleGlobalException_ShouldReturn500() {
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<Object> response = exceptionController.handleGlobalException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().toString().contains("An unexpected error occurred"));
    }

    @Test
    void handleBadRequestException_ShouldReturn400() {
        BadRequestException exception = new BadRequestException("Bad request error");

        ResponseEntity<Object> response = exceptionController.handleBadRequestException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().toString().contains("Bad request error"));
    }

    @Test
    void handleIllegalArgumentException_ShouldReturn400() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        ResponseEntity<Object> response = exceptionController.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().toString().contains("Invalid argument"));
    }

    @Test
    void handleIndexOutOfBoundsException_ShouldReturn404() {
        IndexOutOfBoundsException exception = new IndexOutOfBoundsException("Index out of bounds");

        ResponseEntity<Object> response = exceptionController.handleIndexOutOfBoundsException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().toString().contains("Index out of bounds"));
    }

    @Test
    void handleConflictException_ShouldReturn409() {
        ConflictException exception = new ConflictException("Conflict occurred");

        ResponseEntity<Object> response = exceptionController.handleConflictException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().toString().contains("Conflict occurred"));
    }

    @Test
    void handleTeapotException_ShouldReturn418() {
        TeapotException exception = new TeapotException("I'm a teapot");

        ResponseEntity<Object> response = exceptionController.handleTeapotException(exception);

        assertEquals(HttpStatus.I_AM_A_TEAPOT, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().toString().contains("I'm a teapot"));
    }

    @Test
    void handleAuthenticationException_ShouldReturn401() {
        AuthenticationException exception = mock(AuthenticationException.class);
        when(exception.getMessage()).thenReturn("Authentication failed");

        ResponseEntity<Object> response = exceptionController.handleAuthenticationException(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().toString().contains("Invalid username or password"));
    }

    @Test
    void handleMissingServletRequestParameterException_ShouldReturn400() {
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException("customerId",
                "Long");

        ResponseEntity<Object> response = exceptionController.handleMissingServletRequestParameterException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().toString().contains("customerId"));
        assertTrue(response.getBody().toString().contains("Missing Parameter"));
    }

    @Test
    void handleValidationException_ShouldReturn400() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        FieldError fieldError = new FieldError("object", "field", "Field is invalid");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<Object> response = exceptionController.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Validation Error", responseBody.get("error"));
        assertEquals("One or more validation errors occurred", responseBody.get("message"));
        assertTrue(((Map<String, String>) responseBody.get("fieldErrors")).containsKey("field"));
        assertEquals("Field is invalid", ((Map<String, String>) responseBody.get("fieldErrors")).get("field"));
    }

    @Test
    void buildErrorResponse_ShouldCreateConsistentStructure() {
        ResponseEntity<Object> response = exceptionController.handleBadRequestException(
                new BadRequestException("Test message"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertTrue(responseBody.containsKey("timestamp"));
        assertTrue(responseBody.containsKey("status"));
        assertTrue(responseBody.containsKey("error"));
        assertTrue(responseBody.containsKey("message"));

        assertEquals(400, responseBody.get("status"));
        assertEquals("Bad Request", responseBody.get("error"));
        assertEquals("Test message", responseBody.get("message"));
    }
}
