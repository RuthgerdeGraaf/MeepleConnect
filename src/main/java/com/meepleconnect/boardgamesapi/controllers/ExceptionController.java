package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.exceptions.BadRequestException;
import com.meepleconnect.boardgamesapi.exceptions.ConflictException;
import com.meepleconnect.boardgamesapi.exceptions.GameNotFoundException;
import com.meepleconnect.boardgamesapi.exceptions.ReservationNotFoundException;
import com.meepleconnect.boardgamesapi.exceptions.TeapotException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler voor de MeepleConnect Boardgames API.
 * 
 * Deze klasse handelt alle exceptions af die kunnen optreden in de applicatie
 * en zorgt voor consistente error responses. Het gebruikt @ControllerAdvice
 * om automatisch alle exceptions op te vangen die niet lokaal worden
 * afgehandeld.
 * 
 * Belangrijke kenmerken:
 * - Consistente error response structuur
 * - Logging van alle exceptions voor debugging
 * - Specifieke HTTP status codes voor verschillende exception types
 * - Gebruiksvriendelijke error messages
 */
@ControllerAdvice
public class ExceptionController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    /**
     * Handelt BadRequestException af - wordt gebruikt voor ongeldige input data
     */
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        logger.warn("Bad Request Exception: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    /**
     * Handelt IllegalArgumentException af - wordt gebruikt voor ongeldige method
     * parameters
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("Illegal Argument Exception: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Illegal Argument", ex.getMessage());
    }

    /**
     * Handelt GameNotFoundException af - wordt gebruikt wanneer een bordspel niet
     * gevonden wordt
     */
    @ExceptionHandler(value = GameNotFoundException.class)
    public ResponseEntity<Object> handleGameNotFoundException(GameNotFoundException ex) {
        logger.info("Game Not Found Exception: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Game Not Found", ex.getMessage());
    }

    /**
     * Handelt ReservationNotFoundException af - wordt gebruikt wanneer een
     * reservering niet gevonden wordt
     */
    @ExceptionHandler(value = ReservationNotFoundException.class)
    public ResponseEntity<Object> handleReservationNotFoundException(ReservationNotFoundException ex) {
        logger.info("Reservation Not Found Exception: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Reservation Not Found", ex.getMessage());
    }

    /**
     * Handelt IndexOutOfBoundsException af - wordt gebruikt voor array/list index
     * problemen
     */
    @ExceptionHandler(value = IndexOutOfBoundsException.class)
    public ResponseEntity<Object> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
        logger.warn("Index Out Of Bounds Exception: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Index out of bounds", ex.getMessage());
    }

    /**
     * Handelt ConflictException af - wordt gebruikt voor conflicterende data (bijv.
     * dubbele entries)
     */
    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<Object> handleConflictException(ConflictException ex) {
        logger.warn("Conflict Exception: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage());
    }

    /**
     * Handelt TeapotException af - easter egg exception voor HTTP 418 status
     */
    @ExceptionHandler(value = TeapotException.class)
    public ResponseEntity<Object> handleTeapotException(TeapotException ex) {
        logger.info("Teapot Exception (Easter Egg): {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.I_AM_A_TEAPOT, "I'm a teapot", ex.getMessage());
    }

    /**
     * Handelt AuthorizationDeniedException af - wordt gebruikt voor
     * toegangscontrole problemen
     */
    @ExceptionHandler(value = AuthorizationDeniedException.class)
    public ResponseEntity<Object> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        logger.warn("Authorization Denied Exception: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Access Denied",
                "You don't have permission to access this resource");
    }

    /**
     * Handelt AuthenticationException af - wordt gebruikt voor authenticatie
     * problemen
     */
    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        logger.warn("Authentication Exception: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication Failed",
                "Invalid username or password");
    }

    /**
     * Handelt MissingServletRequestParameterException af - wordt gebruikt voor
     * ontbrekende request parameters
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        logger.warn("Missing Servlet Request Parameter Exception: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Missing Parameter",
                "Required parameter '" + ex.getParameterName() + "' is missing");
    }

    /**
     * Handelt MethodArgumentNotValidException af - wordt gebruikt voor validatie
     * fouten
     * Geeft gedetailleerde field errors terug voor betere debugging
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        logger.warn("Validation Exception: {}", ex.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Error");
        body.put("message", "One or more validation errors occurred");

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

        body.put("fieldErrors", fieldErrors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Catch-all exception handler voor alle andere exceptions
     * Logt de volledige stack trace voor debugging maar toont geen gevoelige
     * informatie aan de client
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        logger.error("Unhandled Exception occurred: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "An unexpected error occurred. Please try again later.");
    }

    /**
     * Bouwt een consistente error response op met timestamp, status, error type en
     * message
     * 
     * @param status  HTTP status code
     * @param error   Korte beschrijving van het error type
     * @param message Gedetailleerde error message
     * @return ResponseEntity met gestructureerde error response
     */
    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);

        return new ResponseEntity<>(body, status);
    }
}
