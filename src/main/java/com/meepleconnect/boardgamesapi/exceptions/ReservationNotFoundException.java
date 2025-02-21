package com.meepleconnect.boardgamesapi.exceptions;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(Long reservationId) {
        super("Reservering met ID " + reservationId + " niet gevonden.");
    }

    public ReservationNotFoundException(String message) {
        super(message);
    }
}
