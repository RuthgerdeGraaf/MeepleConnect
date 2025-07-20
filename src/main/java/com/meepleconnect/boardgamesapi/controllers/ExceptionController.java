package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class ExceptionController {

    public static class ErrorResponse {
        private final String error;
        private final String message;
        private final LocalDateTime timestamp;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
            this.timestamp = LocalDateTime.now();
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        ErrorResponse error = new ErrorResponse("Bad Request", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse("Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGameNotFoundException(GameNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("Game Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReservationNotFoundException(ReservationNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("Reservation Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(PublisherNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePublisherNotFoundException(PublisherNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("Publisher Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("User Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFoundException(FileNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("File Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        ErrorResponse error = new ErrorResponse("Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        ErrorResponse error = new ErrorResponse("Conflict", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(TeapotException.class)
    public ResponseEntity<ErrorResponse> handleTeapotException(TeapotException ex) {
        ErrorResponse error = new ErrorResponse("I'm a Teapot", ex.getMessage());
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(error);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponse> handleFileUploadException(FileUploadException ex) {
        ErrorResponse error = new ErrorResponse("File Upload Error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        ErrorResponse error = new ErrorResponse("Unauthorized", "Invalid credentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex) {
        ErrorResponse error = new ErrorResponse("Unsupported Media Type", "Content type not supported");
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(error);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        ErrorResponse error = new ErrorResponse("Bad Request", "Invalid request data");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            org.springframework.web.bind.MissingServletRequestParameterException ex) {
        ErrorResponse error = new ErrorResponse("Bad Request", "Missing required parameter: " + ex.getParameterName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex) {
        ErrorResponse error = new ErrorResponse("Bad Request", "Invalid parameter type for: " + ex.getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(java.sql.SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(java.sql.SQLException ex) {
        if (ex.getMessage().contains("does not exist")) {
            ErrorResponse error = new ErrorResponse("Service Unavailable",
                    "Database schema issue - please contact administrator");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        } else if (ex.getMessage().contains("null value")) {
            ErrorResponse error = new ErrorResponse("Bad Request",
                    "Invalid data - required fields cannot be empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        ErrorResponse error = new ErrorResponse("Database Error", "Database operation failed");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse("Internal Server Error", "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
