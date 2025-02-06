package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.models.Reservation;
import com.meepleconnect.boardgamesapi.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            @RequestParam Long customerId,
            @RequestParam Long boardgameId,
            @RequestParam String reservationDate,
            @RequestParam int participantCount,
            @RequestParam(required = false) String notes) {

        LocalDate date = LocalDate.parse(reservationDate);
        return ResponseEntity.ok(reservationService.createReservation(customerId, boardgameId, date, participantCount, notes));
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}
