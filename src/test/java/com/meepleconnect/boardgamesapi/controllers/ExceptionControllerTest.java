package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ExceptionControllerTest {

    private ExceptionController exceptionController;

    @BeforeEach
    void setUp() {
        exceptionController = new ExceptionController();
    }

    @Test
    void handleValidationException_WithFieldErrors_ShouldReturnBadRequest() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "fieldName", "Field is required");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        // Act
        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController.handleValidationException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Bad Request");
        assertThat(response.getBody().getMessage()).isEqualTo("fieldName: Field is required");
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }

    @Test
    void handleValidationException_WithoutFieldErrors_ShouldReturnBadRequest() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        // Act
        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController.handleValidationException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Bad Request");
        assertThat(response.getBody().getMessage()).isEqualTo("Validation failed");
    }

    @Test
    void handleIllegalArgumentException_ShouldReturnBadRequest() {
        // Arrange
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");

        // Act
        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                .handleIllegalArgumentException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Bad Request");
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid argument");
    }

    @Test
    void handleGameNotFoundException_ShouldReturnNotFound() {
        // Arrange
        GameNotFoundException ex = new GameNotFoundException("Game not found");

        // Act
        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                .handleGameNotFoundException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Game Not Found");
        assertThat(response.getBody().getMessage()).isEqualTo("Game not found");
    }

    @Test
    void handleReservationNotFoundException_ShouldReturnNotFound() {
        // Arrange
        ReservationNotFoundException ex = new ReservationNotFoundException("Reservation not found");

        // Act
        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                .handleReservationNotFoundException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Reservation Not Found");
        assertThat(response.getBody().getMessage()).isEqualTo("Reservation not found");
    }

    @Test
    void handlePublisherNotFoundException_ShouldReturnNotFound() {
        // Arrange
        PublisherNotFoundException ex = new PublisherNotFoundException("Publisher not found");

        // Act
        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                .handlePublisherNotFoundException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Publisher Not Found");
        assertThat(response.getBody().getMessage()).isEqualTo("Publisher not found");
    }

    @Test
    void handleUserNotFoundException_ShouldReturnNotFound() {
        // Arrange
        UserNotFoundException ex = new UserNotFoundException("User not found");

        // Act
        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                .handleUserNotFoundException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("User Not Found");
        assertThat(response.getBody().getMessage()).isEqualTo("User not found");
    }

    @Test
    void handleFileNotFoundException_ShouldReturnNotFound() {
        // Arrange
        FileNotFoundException ex = new FileNotFoundException("File not found");

        // Act
        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                .handleFileNotFoundException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("File Not Found");
        assertThat(response.getBody().getMessage()).isEqualTo("File not found");
    }

    @Test
    void handleBadRequestException_ShouldReturnBadRequest() {
        // Arrange
        BadRequestException ex = new BadRequestException("Bad request");

        // Act
        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController.handleBadRequestException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Bad Request");
        assertThat(response.getBody().getMessage()).isEqualTo("Bad request");
    }

    @Test
    void handleConflictException_ShouldReturnConflict() {
        // Arrange
        ConflictException ex = new ConflictException("Conflict occurred");

        // Act
        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController.handleConflictException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Conflict");
        assertThat(response.getBody().getMessage()).isEqualTo("Conflict occurred");
    }

    @Test
    void handleTeapotException_ShouldReturnTeapot() {
        // Arrange
        TeapotException ex = new TeapotException("I'm a teapot");

        // Act
        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController.handleTeapotException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.I_AM_A_TEAPOT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("I'm a Teapot");
        assertThat(response.getBody().getMessage()).isEqualTo("I'm a teapot");
    }

    @Test
    void handleFileUploadException_ShouldReturnInternalServerError() {
        // Arrange
        FileUploadException ex = new FileUploadException("File upload failed");

        // Act
        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController.handleFileUploadException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("File Upload Error");
        assertThat(response.getBody().getMessage()).isEqualTo("File upload failed");
    }

    @Test
    void handleMissingServletRequestParameterException_ShouldReturnBadRequest() {
        // Arrange
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("paramName", "String");

        // Act
        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                .handleMissingServletRequestParameterException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Bad Request");
        assertThat(response.getBody().getMessage()).isEqualTo("Missing required parameter: paramName");
    }

    @Test
    void handleMethodArgumentTypeMismatchException_ShouldReturnBadRequest() {
        // Arrange
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getName()).thenReturn("paramName");

        // Act
        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                .handleMethodArgumentTypeMismatchException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Bad Request");
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid parameter type for: paramName");
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError() {
        // Arrange
        Exception ex = new RuntimeException("Unexpected error");

        // Act
        ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController.handleGenericException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Internal Server Error");
        assertThat(response.getBody().getMessage()).isEqualTo("An unexpected error occurred");
    }

    @Test
    void errorResponse_ShouldHaveCorrectStructure() {
        // Arrange
        String error = "Test Error";
        String message = "Test Message";

        // Act
        ExceptionController.ErrorResponse errorResponse = new ExceptionController.ErrorResponse(error, message);

        // Assert
        assertThat(errorResponse.getError()).isEqualTo(error);
        assertThat(errorResponse.getMessage()).isEqualTo(message);
        assertThat(errorResponse.getTimestamp()).isNotNull();
        assertThat(errorResponse.getTimestamp()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    void errorResponse_TimestampShouldBeRecent() {
        // Arrange
        LocalDateTime beforeCreation = LocalDateTime.now().minusSeconds(1);

        // Act
        ExceptionController.ErrorResponse errorResponse = new ExceptionController.ErrorResponse("Error", "Message");

        // Assert
        assertThat(errorResponse.getTimestamp()).isAfter(beforeCreation);
        assertThat(errorResponse.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now().plusSeconds(1));
    }

    @Test
    void allExceptionHandlers_ShouldReturnNonNullResponse() {
        // Test that all exception handlers return non-null responses
        // Create proper mock for MethodArgumentNotValidException
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());
        MethodArgumentNotValidException validationEx = mock(MethodArgumentNotValidException.class);
        when(validationEx.getBindingResult()).thenReturn(bindingResult);

        assertThat(exceptionController.handleValidationException(validationEx)).isNotNull();
        assertThat(exceptionController.handleIllegalArgumentException(new IllegalArgumentException("test")))
                .isNotNull();
        assertThat(exceptionController.handleGameNotFoundException(new GameNotFoundException("test"))).isNotNull();
        assertThat(exceptionController.handleReservationNotFoundException(new ReservationNotFoundException("test")))
                .isNotNull();
        assertThat(exceptionController.handlePublisherNotFoundException(new PublisherNotFoundException("test")))
                .isNotNull();
        assertThat(exceptionController.handleUserNotFoundException(new UserNotFoundException("test"))).isNotNull();
        assertThat(exceptionController.handleFileNotFoundException(new FileNotFoundException("test"))).isNotNull();
        assertThat(exceptionController.handleBadRequestException(new BadRequestException("test"))).isNotNull();
        assertThat(exceptionController.handleConflictException(new ConflictException("test"))).isNotNull();
        assertThat(exceptionController.handleTeapotException(new TeapotException("test"))).isNotNull();
        assertThat(exceptionController.handleFileUploadException(new FileUploadException("test"))).isNotNull();
        assertThat(exceptionController.handleMissingServletRequestParameterException(
                new MissingServletRequestParameterException("test", "String"))).isNotNull();
        assertThat(exceptionController
                .handleMethodArgumentTypeMismatchException(mock(MethodArgumentTypeMismatchException.class)))
                .isNotNull();
        assertThat(exceptionController.handleGenericException(new Exception("test"))).isNotNull();
    }

    @Test
    void allExceptionHandlers_ShouldReturnNonNullBody() {
        // Test that all exception handlers return responses with non-null body
        // Create proper mock for MethodArgumentNotValidException
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());
        MethodArgumentNotValidException validationEx = mock(MethodArgumentNotValidException.class);
        when(validationEx.getBindingResult()).thenReturn(bindingResult);

        assertThat(exceptionController.handleValidationException(validationEx).getBody()).isNotNull();
        assertThat(exceptionController.handleIllegalArgumentException(new IllegalArgumentException("test")).getBody())
                .isNotNull();
        assertThat(exceptionController.handleGameNotFoundException(new GameNotFoundException("test")).getBody())
                .isNotNull();
        assertThat(exceptionController.handleReservationNotFoundException(new ReservationNotFoundException("test"))
                .getBody()).isNotNull();
        assertThat(
                exceptionController.handlePublisherNotFoundException(new PublisherNotFoundException("test")).getBody())
                .isNotNull();
        assertThat(exceptionController.handleUserNotFoundException(new UserNotFoundException("test")).getBody())
                .isNotNull();
        assertThat(exceptionController.handleFileNotFoundException(new FileNotFoundException("test")).getBody())
                .isNotNull();
        assertThat(exceptionController.handleBadRequestException(new BadRequestException("test")).getBody())
                .isNotNull();
        assertThat(exceptionController.handleConflictException(new ConflictException("test")).getBody()).isNotNull();
        assertThat(exceptionController.handleTeapotException(new TeapotException("test")).getBody()).isNotNull();
        assertThat(exceptionController.handleFileUploadException(new FileUploadException("test")).getBody())
                .isNotNull();
        assertThat(exceptionController.handleMissingServletRequestParameterException(
                new MissingServletRequestParameterException("test", "String")).getBody()).isNotNull();
        assertThat(exceptionController
                .handleMethodArgumentTypeMismatchException(mock(MethodArgumentTypeMismatchException.class)).getBody())
                .isNotNull();
        assertThat(exceptionController.handleGenericException(new Exception("test")).getBody()).isNotNull();
    }

    @Test
    void exceptionHandlers_ShouldPreserveExceptionMessage() {
        // Test that exception messages are preserved in responses
        String testMessage = "Custom test message";

        assertThat(exceptionController.handleIllegalArgumentException(new IllegalArgumentException(testMessage))
                .getBody().getMessage()).isEqualTo(testMessage);
        assertThat(exceptionController.handleGameNotFoundException(new GameNotFoundException(testMessage))
                .getBody().getMessage()).isEqualTo(testMessage);
        assertThat(exceptionController.handleReservationNotFoundException(new ReservationNotFoundException(testMessage))
                .getBody().getMessage()).isEqualTo(testMessage);
        assertThat(exceptionController.handlePublisherNotFoundException(new PublisherNotFoundException(testMessage))
                .getBody().getMessage()).isEqualTo(testMessage);
        assertThat(exceptionController.handleUserNotFoundException(new UserNotFoundException(testMessage))
                .getBody().getMessage()).isEqualTo(testMessage);
        assertThat(exceptionController.handleFileNotFoundException(new FileNotFoundException(testMessage))
                .getBody().getMessage()).isEqualTo(testMessage);
        assertThat(exceptionController.handleBadRequestException(new BadRequestException(testMessage))
                .getBody().getMessage()).isEqualTo(testMessage);
        assertThat(exceptionController.handleConflictException(new ConflictException(testMessage))
                .getBody().getMessage()).isEqualTo(testMessage);
        assertThat(exceptionController.handleTeapotException(new TeapotException(testMessage))
                .getBody().getMessage()).isEqualTo(testMessage);
        assertThat(exceptionController.handleFileUploadException(new FileUploadException(testMessage))
                .getBody().getMessage()).isEqualTo(testMessage);
    }

    @Test
    void errorResponse_ShouldBeImmutable() {
        // Test that ErrorResponse fields are immutable (final)
        ExceptionController.ErrorResponse errorResponse = new ExceptionController.ErrorResponse("Error", "Message");

        // The fields should be final and not changeable
        LocalDateTime originalTimestamp = errorResponse.getTimestamp();

        // Wait a bit to ensure time has passed
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Timestamp should remain the same (immutable)
        assertThat(errorResponse.getTimestamp()).isEqualTo(originalTimestamp);
    }
}