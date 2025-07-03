package com.meepleconnect.boardgamesapi.services;

import com.meepleconnect.boardgamesapi.entities.User;
import com.meepleconnect.boardgamesapi.exceptions.GameNotFoundException;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.models.Reservation;
import com.meepleconnect.boardgamesapi.repositories.BoardgameRepository;
import com.meepleconnect.boardgamesapi.repositories.ReservationRepository;
import com.meepleconnect.boardgamesapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private BoardgameRepository boardgameRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    private User testUser;
    private Boardgame testBoardgame;
    private Reservation testReservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User(1L);
        testUser.setUserName("testuser");
        testUser.setPassword("password");

        testBoardgame = new Boardgame("Test Game", new BigDecimal("29.99"), true, 2, 4, "Strategy", null);

        testReservation = new Reservation(testUser, testBoardgame, LocalDate.now().plusDays(1), 3, "Test reservation");
    }

    @Test
    void getAllReservations_ShouldReturnList() {
        when(reservationRepository.findAll()).thenReturn(List.of(testReservation));

        List<Reservation> result = reservationService.getAllReservations();

        assertEquals(1, result.size());
        assertEquals(testReservation, result.get(0));
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void getReservationsByCustomer_ShouldReturnList() {
        when(reservationRepository.findByCustomerId(1L)).thenReturn(List.of(testReservation));

        List<Reservation> result = reservationService.getReservationsByCustomer(1L);

        assertEquals(1, result.size());
        assertEquals(testReservation, result.get(0));
        verify(reservationRepository, times(1)).findByCustomerId(1L);
    }

    @Test
    void getReservationsByBoardgame_ShouldReturnList() {
        when(reservationRepository.findByBoardgameId(1L)).thenReturn(List.of(testReservation));

        List<Reservation> result = reservationService.getReservationsByBoardgame(1L);

        assertEquals(1, result.size());
        assertEquals(testReservation, result.get(0));
        verify(reservationRepository, times(1)).findByBoardgameId(1L);
    }

    @Test
    void createReservation_ValidData_ShouldCreateReservation() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(boardgameRepository.findById(1L)).thenReturn(Optional.of(testBoardgame));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        Reservation result = reservationService.createReservation(1L, 1L, LocalDate.now().plusDays(1), 3, "Test");

        assertNotNull(result);
        assertEquals(testReservation, result);
        verify(userRepository, times(1)).findById(1L);
        verify(boardgameRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void createReservation_CustomerNotFound_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class,
                () -> reservationService.createReservation(1L, 1L, LocalDate.now().plusDays(1), 3, "Test"));

        verify(userRepository, times(1)).findById(1L);
        verify(boardgameRepository, never()).findById(any());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createReservation_BoardgameNotFound_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(boardgameRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class,
                () -> reservationService.createReservation(1L, 1L, LocalDate.now().plusDays(1), 3, "Test"));

        verify(userRepository, times(1)).findById(1L);
        verify(boardgameRepository, times(1)).findById(1L);
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void cancelReservation_ExistingId_ShouldDeleteReservation() {
        when(reservationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reservationRepository).deleteById(1L);

        assertDoesNotThrow(() -> reservationService.cancelReservation(1L));

        verify(reservationRepository, times(1)).existsById(1L);
        verify(reservationRepository, times(1)).deleteById(1L);
    }

    @Test
    void cancelReservation_NonExistingId_ShouldThrowException() {
        when(reservationRepository.existsById(1L)).thenReturn(false);

        assertThrows(GameNotFoundException.class, () -> reservationService.cancelReservation(1L));

        verify(reservationRepository, times(1)).existsById(1L);
        verify(reservationRepository, never()).deleteById(any());
    }
}