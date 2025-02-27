package com.meepleconnect.boardgamesapi.services;

import com.meepleconnect.boardgamesapi.exceptions.GameNotFoundException;
import com.meepleconnect.boardgamesapi.exceptions.ReservationNotFoundException;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.models.Reservation;
import com.meepleconnect.boardgamesapi.models.User;
import com.meepleconnect.boardgamesapi.repositories.BoardgameRepository;
import com.meepleconnect.boardgamesapi.repositories.ReservationRepository;
import com.meepleconnect.boardgamesapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private BoardgameRepository boardgameRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;
    private User user;
    private Boardgame boardgame;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("TestUser");

        boardgame = new Boardgame();
        boardgame.setId(1L);
        boardgame.setName("Catan");

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setBoardgame(boardgame);
        reservation.setReservationDate(LocalDate.now());
    }

    @Test
    void testGetAllReservations() {
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));

        var reservations = reservationService.getAllReservations();
        assertEquals(1, reservations.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testGetReservationByCustomer_Success() {
        when(reservationRepository.findByCustomerId(1L)).thenReturn(Arrays.asList(reservation));

        var result = reservationService.getReservationsByCustomer(1L);
        assertEquals(1, result.size());
        verify(reservationRepository, times(1)).findByCustomerId(1L);
    }

    @Test
    void testGetReservationByCustomer_Failure() {
        when(reservationRepository.findByCustomerId(1L)).thenReturn(Collections.emptyList());

        assertThrows(ReservationNotFoundException.class, () -> reservationService.getReservationsByCustomer(1L));

        verify(reservationRepository, times(1)).findByCustomerId(1L);
    }


    @Test
    void testGetReservationsByBoardgame_Success() {
        when(reservationRepository.findByBoardgameId(1L)).thenReturn(Arrays.asList(reservation));

        var result = reservationService.getReservationsByBoardgame(1L);
        assertEquals(1, result.size());
        verify(reservationRepository, times(1)).findByBoardgameId(1L);
    }

    @Test
    void testGetReservationsByBoardgame_Failure() {
        when(reservationRepository.findByBoardgameId(1L)).thenReturn(Collections.emptyList());
        assertThrows(ReservationNotFoundException.class, () -> reservationService.getReservationsByBoardgame(1L));
        verify(reservationRepository, times(1)).findByBoardgameId(1L);
    }

    @Test
    void testCreateReservation_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(boardgameRepository.findById(1L)).thenReturn(Optional.of(boardgame));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        assertEquals(reservation, reservationService.createReservation(1L, 1L, LocalDate.now(), 1, "Test"));
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

//    @Test
//    void testCreateReservation_Failure() {
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//        when(boardgameRepository.findById(1L)).thenReturn(Optional.of(boardgame));
//        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
//        assertThrows(ReservationNotFoundException.class, () -> reservationService.getReservationsByBoardgame(1L));
//        verify(reservationRepository, times(0)).save(any(Reservation.class));
//    }

    @Test
    void testCreateReservation_Failure_NoCustomer() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(boardgameRepository.findById(1L)).thenReturn(Optional.of(boardgame));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        assertThrows(GameNotFoundException.class, () -> reservationService.createReservation(1L, 1L, LocalDate.now(), 1, "Test"));
        verify(reservationRepository, times(0)).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_Failure_NoBoardgame() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(boardgameRepository.findById(1L)).thenReturn(Optional.empty());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        assertThrows(GameNotFoundException.class, () -> reservationService.createReservation(1L, 1L, LocalDate.now(), 1, "Test"));
        verify(reservationRepository, times(0)).save(any(Reservation.class));
    }
}