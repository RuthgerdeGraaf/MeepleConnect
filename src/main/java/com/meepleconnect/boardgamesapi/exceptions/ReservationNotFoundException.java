package com.meepleconnect.boardgamesapi.exceptions;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(Long reservationId) {
        super("Reservation with ID " + reservationId + " could not be found.");
    }

    public ReservationNotFoundException(String message) {
            super(message);
        }
}
