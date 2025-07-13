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

public class ExceptionControllerIT {

        private ExceptionController exceptionController;

        @BeforeEach
        void setUp() {
                exceptionController = new ExceptionController();
        }

        @Test
        void handleValidationException_WithFieldErrors_ShouldReturnBadRequest() {
                BindingResult bindingResult = mock(BindingResult.class);
                FieldError fieldError = new FieldError("object", "fieldName", "Field is required");
                when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

                MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
                when(ex.getBindingResult()).thenReturn(bindingResult);

                ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                                .handleValidationException(ex);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getError()).isEqualTo("Bad Request");
                assertThat(response.getBody().getMessage()).isEqualTo("fieldName: Field is required");
                assertThat(response.getBody().getTimestamp()).isNotNull();
        }

        @Test
        void handleValidationException_WithoutFieldErrors_ShouldReturnBadRequest() {
                BindingResult bindingResult = mock(BindingResult.class);
                when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

                MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
                when(ex.getBindingResult()).thenReturn(bindingResult);

                ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                                .handleValidationException(ex);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getError()).isEqualTo("Bad Request");
                assertThat(response.getBody().getMessage()).isEqualTo("Validation failed");
        }

        @Test
        void handleIllegalArgumentException_ShouldReturnBadRequest() {
                IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");

                ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                                .handleIllegalArgumentException(ex);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getError()).isEqualTo("Bad Request");
                assertThat(response.getBody().getMessage()).isEqualTo("Invalid argument");
        }

        @Test
        void handleGameNotFoundException_ShouldReturnNotFound() {
                GameNotFoundException ex = new GameNotFoundException("Game not found");

                ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                                .handleGameNotFoundException(ex);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getError()).isEqualTo("Game Not Found");
                assertThat(response.getBody().getMessage()).isEqualTo("Game not found");
        }

        @Test
        void handleReservationNotFoundException_ShouldReturnNotFound() {
                ReservationNotFoundException ex = new ReservationNotFoundException("Reservation not found");

                ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                                .handleReservationNotFoundException(ex);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getError()).isEqualTo("Reservation Not Found");
                assertThat(response.getBody().getMessage()).isEqualTo("Reservation not found");
        }

        @Test
        void handlePublisherNotFoundException_ShouldReturnNotFound() {
                PublisherNotFoundException ex = new PublisherNotFoundException("Publisher not found");

                ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                                .handlePublisherNotFoundException(ex);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getError()).isEqualTo("Publisher Not Found");
                assertThat(response.getBody().getMessage()).isEqualTo("Publisher not found");
        }

        @Test
        void handleUserNotFoundException_ShouldReturnNotFound() {
                UserNotFoundException ex = new UserNotFoundException("User not found");

                ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                                .handleUserNotFoundException(ex);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getError()).isEqualTo("User Not Found");
                assertThat(response.getBody().getMessage()).isEqualTo("User not found");
        }

        @Test
        void handleFileNotFoundException_ShouldReturnNotFound() {
                FileNotFoundException ex = new FileNotFoundException("File not found");

                ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                                .handleFileNotFoundException(ex);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getError()).isEqualTo("File Not Found");
                assertThat(response.getBody().getMessage()).isEqualTo("File not found");
        }

        @Test
        void handleBadRequestException_ShouldReturnBadRequest() {
                BadRequestException ex = new BadRequestException("Bad request");

                ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                                .handleBadRequestException(ex);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getError()).isEqualTo("Bad Request");
                assertThat(response.getBody().getMessage()).isEqualTo("Bad request");
        }

        @Test
        void handleConflictException_ShouldReturnConflict() {
                ConflictException ex = new ConflictException("Conflict occurred");

                ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                                .handleConflictException(ex);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getError()).isEqualTo("Conflict");
                assertThat(response.getBody().getMessage()).isEqualTo("Conflict occurred");
        }

        @Test
        void handleTeapotException_ShouldReturnTeapot() {
                TeapotException ex = new TeapotException("I'm a teapot");

                ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                                .handleTeapotException(ex);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.I_AM_A_TEAPOT);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getError()).isEqualTo("I'm a Teapot");
                assertThat(response.getBody().getMessage()).isEqualTo("I'm a teapot");
        }

        @Test
        void handleFileUploadException_ShouldReturnInternalServerError() {
                FileUploadException ex = new FileUploadException("File upload failed");

                ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                                .handleFileUploadException(ex);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getError()).isEqualTo("File Upload Error");
                assertThat(response.getBody().getMessage()).isEqualTo("File upload failed");
        }

        @Test
        void handleMissingServletRequestParameterException_ShouldReturnBadRequest() {
                MissingServletRequestParameterException ex = new MissingServletRequestParameterException("paramName",
                                "String");

                ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                                .handleMissingServletRequestParameterException(ex);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getError()).isEqualTo("Bad Request");
                assertThat(response.getBody().getMessage()).isEqualTo("Missing required parameter: paramName");
        }

        @Test
        void handleMethodArgumentTypeMismatchException_ShouldReturnBadRequest() {
                MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
                when(ex.getName()).thenReturn("paramName");

                ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                                .handleMethodArgumentTypeMismatchException(ex);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getError()).isEqualTo("Bad Request");
                assertThat(response.getBody().getMessage()).isEqualTo("Invalid parameter type for: paramName");
        }

        @Test
        void handleGenericException_ShouldReturnInternalServerError() {
                Exception ex = new RuntimeException("Unexpected error");

                ResponseEntity<ExceptionController.ErrorResponse> response = exceptionController
                                .handleGenericException(ex);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getError()).isEqualTo("Internal Server Error");
                assertThat(response.getBody().getMessage()).isEqualTo("An unexpected error occurred");
        }

        @Test
        void errorResponse_ShouldHaveCorrectStructure() {
                String error = "Test Error";
                String message = "Test Message";

                ExceptionController.ErrorResponse errorResponse = new ExceptionController.ErrorResponse(error, message);

                assertThat(errorResponse.getError()).isEqualTo(error);
                assertThat(errorResponse.getMessage()).isEqualTo(message);
                assertThat(errorResponse.getTimestamp()).isNotNull();
                assertThat(errorResponse.getTimestamp()).isInstanceOf(LocalDateTime.class);
        }

        @Test
        void errorResponse_TimestampShouldBeRecent() {
                LocalDateTime beforeCreation = LocalDateTime.now().minusSeconds(1);

                ExceptionController.ErrorResponse errorResponse = new ExceptionController.ErrorResponse("Error",
                                "Message");

                assertThat(errorResponse.getTimestamp()).isAfter(beforeCreation);
                assertThat(errorResponse.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now().plusSeconds(1));
        }

        @Test
        void allExceptionHandlers_ShouldReturnNonNullResponse() {
                BindingResult bindingResult = mock(BindingResult.class);
                when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());
                MethodArgumentNotValidException validationEx = mock(MethodArgumentNotValidException.class);
                when(validationEx.getBindingResult()).thenReturn(bindingResult);

                assertThat(exceptionController.handleValidationException(validationEx)).isNotNull();
                assertThat(exceptionController.handleIllegalArgumentException(new IllegalArgumentException("test")))
                                .isNotNull();
                assertThat(exceptionController.handleGameNotFoundException(new GameNotFoundException("test")))
                                .isNotNull();
                assertThat(exceptionController
                                .handleReservationNotFoundException(new ReservationNotFoundException("test")))
                                .isNotNull();
                assertThat(exceptionController.handlePublisherNotFoundException(new PublisherNotFoundException("test")))
                                .isNotNull();
                assertThat(exceptionController.handleUserNotFoundException(new UserNotFoundException("test")))
                                .isNotNull();
                assertThat(exceptionController.handleFileNotFoundException(new FileNotFoundException("test")))
                                .isNotNull();
                assertThat(exceptionController.handleBadRequestException(new BadRequestException("test"))).isNotNull();
                assertThat(exceptionController.handleConflictException(new ConflictException("test"))).isNotNull();
                assertThat(exceptionController.handleTeapotException(new TeapotException("test"))).isNotNull();
                assertThat(exceptionController.handleFileUploadException(new FileUploadException("test"))).isNotNull();
                assertThat(exceptionController.handleMissingServletRequestParameterException(
                                new MissingServletRequestParameterException("test", "String"))).isNotNull();
                assertThat(exceptionController
                                .handleMethodArgumentTypeMismatchException(
                                                mock(MethodArgumentTypeMismatchException.class)))
                                .isNotNull();
                assertThat(exceptionController.handleGenericException(new Exception("test"))).isNotNull();
        }

        @Test
        void allExceptionHandlers_ShouldReturnNonNullBody() {
                BindingResult bindingResult = mock(BindingResult.class);
                when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());
                MethodArgumentNotValidException validationEx = mock(MethodArgumentNotValidException.class);
                when(validationEx.getBindingResult()).thenReturn(bindingResult);

                assertThat(exceptionController.handleValidationException(validationEx).getBody()).isNotNull();
                assertThat(exceptionController.handleIllegalArgumentException(new IllegalArgumentException("test"))
                                .getBody())
                                .isNotNull();
                assertThat(exceptionController.handleGameNotFoundException(new GameNotFoundException("test")).getBody())
                                .isNotNull();
                assertThat(exceptionController
                                .handleReservationNotFoundException(new ReservationNotFoundException("test"))
                                .getBody()).isNotNull();
                assertThat(
                                exceptionController.handlePublisherNotFoundException(
                                                new PublisherNotFoundException("test")).getBody())
                                .isNotNull();
                assertThat(exceptionController.handleUserNotFoundException(new UserNotFoundException("test")).getBody())
                                .isNotNull();
                assertThat(exceptionController.handleFileNotFoundException(new FileNotFoundException("test")).getBody())
                                .isNotNull();
                assertThat(exceptionController.handleBadRequestException(new BadRequestException("test")).getBody())
                                .isNotNull();
                assertThat(exceptionController.handleConflictException(new ConflictException("test")).getBody())
                                .isNotNull();
                assertThat(exceptionController.handleTeapotException(new TeapotException("test")).getBody())
                                .isNotNull();
                assertThat(exceptionController.handleFileUploadException(new FileUploadException("test")).getBody())
                                .isNotNull();
                assertThat(exceptionController.handleMissingServletRequestParameterException(
                                new MissingServletRequestParameterException("test", "String")).getBody()).isNotNull();
                assertThat(exceptionController
                                .handleMethodArgumentTypeMismatchException(
                                                mock(MethodArgumentTypeMismatchException.class))
                                .getBody())
                                .isNotNull();
                assertThat(exceptionController.handleGenericException(new Exception("test")).getBody()).isNotNull();
        }

        @Test
        void exceptionHandlers_ShouldPreserveExceptionMessage() {
                String testMessage = "Custom test message";

                assertThat(exceptionController.handleIllegalArgumentException(new IllegalArgumentException(testMessage))
                                .getBody().getMessage()).isEqualTo(testMessage);
                assertThat(exceptionController.handleGameNotFoundException(new GameNotFoundException(testMessage))
                                .getBody().getMessage()).isEqualTo(testMessage);
                assertThat(exceptionController
                                .handleReservationNotFoundException(new ReservationNotFoundException(testMessage))
                                .getBody().getMessage()).isEqualTo(testMessage);
                assertThat(exceptionController
                                .handlePublisherNotFoundException(new PublisherNotFoundException(testMessage))
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
                ExceptionController.ErrorResponse errorResponse = new ExceptionController.ErrorResponse("Error",
                                "Message");

                LocalDateTime originalTimestamp = errorResponse.getTimestamp();

                try {
                        Thread.sleep(10);
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                }

                assertThat(errorResponse.getTimestamp()).isEqualTo(originalTimestamp);
        }
}