package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.exceptions.GameNotFoundException;
import com.meepleconnect.boardgamesapi.models.Reservation;
import com.meepleconnect.boardgamesapi.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/customer/{customerId}")
    public List<Reservation> getReservationsByCustomer(@PathVariable Long customerId) {
        return reservationService.getReservationsByCustomer(customerId);
    }

    @GetMapping("/boardgame/{boardgameId}")
    public List<Reservation> getReservationsByBoardgame(@PathVariable Long boardgameId) {
        return reservationService.getReservationsByBoardgame(boardgameId);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @RequestParam(required = true) Long customerId,
            @RequestParam(required = true) Long boardgameId,
            @RequestParam(required = true) String reservationDate,
            @RequestParam(required = true) int participantCount,
            @RequestParam(required = false) String notes) {

        if (participantCount <= 0) {
            return ResponseEntity.badRequest().build();
        }

        try {
            LocalDate date = LocalDate.parse(reservationDate);
            return ResponseEntity.ok(reservationService.createReservation(customerId, boardgameId, date, participantCount, notes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Void> handleMissingParameter(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().build();
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        try {
            reservationService.cancelReservation(reservationId);
            return ResponseEntity.noContent().build();
        } catch (GameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
