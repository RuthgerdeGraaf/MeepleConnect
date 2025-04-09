package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.exceptions.BadRequestException;
import com.meepleconnect.boardgamesapi.exceptions.ConflictException;
import com.meepleconnect.boardgamesapi.exceptions.GameNotFoundException;
import com.meepleconnect.boardgamesapi.exceptions.TeapotException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ExceptionControllerTest {

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
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().toString().contains("Bordspel niet gevonden"));
    }

    @Test
    void handleGlobalException_ShouldReturn500() {
        Exception exception = new Exception("Onverwachte fout");

        ResponseEntity<Object> response = exceptionController.handleGlobalException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().toString().contains("Onverwachte fout"));
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
        IllegalArgumentException exception = new IllegalArgumentException("Ongeldig argument");

        ResponseEntity<Object> response = exceptionController.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().toString().contains("Ongeldig argument"));
    }

    @Test
    void handleIndexOutOfBoundsException_ShouldReturn404() {
        IndexOutOfBoundsException exception = new IndexOutOfBoundsException("Index buiten bereik");

        ResponseEntity<Object> response = exceptionController.handleIndexOutOfBoundsException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().toString().contains("Index buiten bereik"));
    }

    @Test
    void handleConflictException_ShouldReturn409() {
        ConflictException exception = new ConflictException("Conflict opgetreden");

        ResponseEntity<Object> response = exceptionController.handleConflictException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().toString().contains("Conflict opgetreden"));
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
    void handleValidationException_ShouldReturn400() {
        // Mock MethodArgumentNotValidException
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
        assertTrue(((Map<String, String>) responseBody.get("fieldErrors")).containsKey("field"));
        assertEquals("Field is invalid", ((Map<String, String>) responseBody.get("fieldErrors")).get("field"));
    }
}
