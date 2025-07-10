package com.meepleconnect.boardgamesapi.services;

import com.meepleconnect.boardgamesapi.exceptions.GameNotFoundException;
import com.meepleconnect.boardgamesapi.exceptions.ReservationNotFoundException;
import com.meepleconnect.boardgamesapi.models.Reservation;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.entities.User;
import com.meepleconnect.boardgamesapi.repositories.ReservationRepository;
import com.meepleconnect.boardgamesapi.repositories.BoardgameRepository;
import com.meepleconnect.boardgamesapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BoardgameRepository boardgameRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, BoardgameRepository boardgameRepository,
            UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.boardgameRepository = boardgameRepository;
        this.userRepository = userRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByCustomer(Long customerId) {
        return reservationRepository.findByCustomerId(customerId);
    }

    public List<Reservation> getReservationsByBoardgame(Long boardgameId) {
        return reservationRepository.findByBoardgameId(boardgameId);
    }

    public Reservation createReservation(Long customerId, Long boardgameId, LocalDate reservationDate,
            int participantCount, String notes) {
        Optional<User> customer = userRepository.findById(customerId);
        Optional<Boardgame> boardgame = boardgameRepository.findById(boardgameId);

        if (customer.isEmpty()) {
            throw new GameNotFoundException("Customer with ID " + customerId + " wasn't found.");
        }

        if (boardgame.isEmpty()) {
            throw new GameNotFoundException("Boardgame with ID " + boardgameId + " wasn't found.");
        }

        Reservation reservation = new Reservation(customer.get(), boardgame.get(), reservationDate, participantCount,
                notes);
        return reservationRepository.save(reservation);
    }

    public void cancelReservation(Long reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new GameNotFoundException("Reservation with ID " + reservationId + " wasn't found.");
        }
        reservationRepository.deleteById(reservationId);
    }

    public long getTotalReservationsCount() {
        return reservationRepository.count();
    }

    public long getActiveReservationsCount() {
        return reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getReservationDate().isAfter(LocalDate.now()))
                .count();
    }

    public Map<String, Object> getMonthlyReservations(int year, int month) {
        Map<String, Object> monthlyStats = new HashMap<>();
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Reservation> monthlyReservations = reservationRepository.findAll().stream()
                .filter(reservation -> !reservation.getReservationDate().isBefore(startDate) &&
                        !reservation.getReservationDate().isAfter(endDate))
                .toList();

        monthlyStats.put("year", year);
        monthlyStats.put("month", month);
        monthlyStats.put("totalReservations", monthlyReservations.size());
        monthlyStats.put("totalParticipants", monthlyReservations.stream()
                .mapToInt(Reservation::getParticipantCount)
                .sum());

        return monthlyStats;
    }
}
