package com.meepleconnect.boardgamesapi.repositories;

import com.meepleconnect.boardgamesapi.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByCustomerId(Long customerId);
    List<Reservation> findByBoardgameId(Long boardgameId);
}
