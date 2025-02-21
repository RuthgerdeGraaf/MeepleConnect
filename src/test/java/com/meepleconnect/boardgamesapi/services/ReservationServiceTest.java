package com.meepleconnect.boardgamesapi.services;

import com.meepleconnect.boardgamesapi.exceptions.ReservationNotFoundException;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.models.Reservation;
import com.meepleconnect.boardgamesapi.models.User;
import com.meepleconnect.boardgamesapi.repositories.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

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
        reservation.setUser(user);
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
    void testGetReservationById_Success() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        var result = reservationService.getReservationsByCustomer(1L);
        assertEquals(reservation, result);
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetReservationById_NotFound() {
        when(reservationRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> reservationService.getReservationsByCustomer(2L));
        verify(reservationRepository, times(1)).findById(2L);
    }

    @Test
    void testCreateReservation() {
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        var createdReservation = reservationService.createReservation(reservation);
        assertEquals(reservation, createdReservation);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testDeleteReservation_Success() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        doNothing().when(reservationRepository).deleteById(1L);

        reservationService.deleteReservation(1L);
        verify(reservationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteReservation_NotFound() {
        when(reservationRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> reservationService.deleteReservation(2L));
        verify(reservationRepository, times(1)).findById(2L);
    }
}
