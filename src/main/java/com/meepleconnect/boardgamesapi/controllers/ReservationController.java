package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.dtos.ReservationRequestDTO;
import com.meepleconnect.boardgamesapi.exceptions.BadRequestException;
import com.meepleconnect.boardgamesapi.models.Reservation;
import com.meepleconnect.boardgamesapi.services.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
            @Valid @RequestBody ReservationRequestDTO reservationRequestDTO) {
        if (reservationRequestDTO.getParticipantCount() <= 0) {
            throw new BadRequestException("Number of participants must be greater than 0.");
        }

        Reservation reservation = reservationService.createReservation(
                reservationRequestDTO.getCustomerId(),
                reservationRequestDTO.getBoardgameId(),
                reservationRequestDTO.getReservationDate(),
                reservationRequestDTO.getParticipantCount(),
                reservationRequestDTO.getNotes());
        URI location = URI.create("/api/reservations/" + reservation.getId());
        return ResponseEntity.created(location).body(reservation);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}
