package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.exceptions.BadRequestException;
import com.meepleconnect.boardgamesapi.models.Reservation;
import com.meepleconnect.boardgamesapi.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

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

    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @RequestParam(required = true) Long customerId,
            @RequestParam(required = true) Long boardgameId,
            @RequestParam(required = true) String reservationDate,
            @RequestParam(required = true) int participantCount,
            @RequestParam(required = false) String notes) {

        if (participantCount <= 0) {
            throw new BadRequestException("Aantal deelnemers moet groter zijn dan 0.");
        }

        try {
            LocalDate date = LocalDate.parse(reservationDate);
            Reservation reservation = reservationService.createReservation(customerId, boardgameId, date, participantCount, notes);
            return ResponseEntity.ok(reservation);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Ongeldige datum format. Gebruik YYYY-MM-DD.");
        }
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}
