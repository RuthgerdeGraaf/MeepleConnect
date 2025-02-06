package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.models.Reservation;
import com.meepleconnect.boardgamesapi.repositories.ReservationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;

    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        return reservation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public List<Reservation> getReservationsByCustomer(@PathVariable Long customerId) {
        return reservationRepository.findByCustomerId(customerId);
    }

    @GetMapping("/boardgame/{boardgameId}")
    public List<Reservation> getReservationsByBoardgame(@PathVariable Long boardgameId) {
        return reservationRepository.findByBoardgameId(boardgameId);
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation savedReservation = reservationRepository.save(reservation);
        return ResponseEntity.ok(savedReservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation updatedReservation) {
        return reservationRepository.findById(id).map(existingReservation -> {
            existingReservation.setCustomer(updatedReservation.getCustomer());
            existingReservation.setBoardgame(updatedReservation.getBoardgame());
            existingReservation.setReservationDate(updatedReservation.getReservationDate());

            reservationRepository.save(existingReservation);
            return ResponseEntity.ok(existingReservation);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
