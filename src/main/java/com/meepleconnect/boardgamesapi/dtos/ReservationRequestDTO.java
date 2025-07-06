package com.meepleconnect.boardgamesapi.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ReservationRequestDTO {

    @NotNull(message = "Customer ID is required")
    @Min(value = 1, message = "Customer ID must be greater than 0")
    private Long customerId;

    @NotNull(message = "Boardgame ID is required")
    @Min(value = 1, message = "Boardgame ID must be greater than 0")
    private Long boardgameId;

    @NotNull(message = "Reservation date is required")
    @Future(message = "Reservation date must be in the future")
    private LocalDate reservationDate;

    @NotNull(message = "Number of participants is required")
    @Min(value = 1, message = "Number of participants must be at least 1")
    @Max(value = 20, message = "Number of participants cannot be more than 20")
    private Integer participantCount;

    @Size(max = 500, message = "Notes cannot contain more than 500 characters")
    private String notes;
}