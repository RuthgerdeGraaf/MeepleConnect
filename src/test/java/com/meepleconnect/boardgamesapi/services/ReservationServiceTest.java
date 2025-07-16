package com.meepleconnect.boardgamesapi.services;

import com.meepleconnect.boardgamesapi.entities.Role;
import com.meepleconnect.boardgamesapi.entities.User;
import com.meepleconnect.boardgamesapi.exceptions.GameNotFoundException;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.models.Publisher;
import com.meepleconnect.boardgamesapi.models.Reservation;
import com.meepleconnect.boardgamesapi.repositories.BoardgameRepository;
import com.meepleconnect.boardgamesapi.repositories.ReservationRepository;
import com.meepleconnect.boardgamesapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
    private User testUser2;
    private Publisher testPublisher;
    private Boardgame testBoardgame;
    private Boardgame testBoardgame2;
    private Reservation testReservation;
    private Reservation testReservation2;

    @BeforeEach
    void setUp() {
        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setRoleName("USER");

        testUser = new User();
        testUser.setUserName("testuser");
        testUser.setPassword("password");
        testUser.setRoles(List.of(userRole));

        testUser2 = new User();
        testUser2.setUserName("testuser2");
        testUser2.setPassword("password");
        testUser2.setRoles(List.of(userRole));

        testPublisher = new Publisher();
        testPublisher.setName("Test Publisher");
        testPublisher.setCountryOfOrigin("Netherlands");
        testPublisher.setFounded(2020);
        testPublisher.setIndie(true);

        testBoardgame = new Boardgame();
        testBoardgame.setName("Test Game");
        testBoardgame.setPrice(new BigDecimal("29.99"));
        testBoardgame.setAvailable(true);
        testBoardgame.setMinPlayers(2);
        testBoardgame.setMaxPlayers(4);
        testBoardgame.setGenre("Strategy");
        testBoardgame.setPublisher(testPublisher);

        testBoardgame2 = new Boardgame();
        testBoardgame2.setName("Second Game");
        testBoardgame2.setPrice(new BigDecimal("39.99"));
        testBoardgame2.setAvailable(false);
        testBoardgame2.setMinPlayers(1);
        testBoardgame2.setMaxPlayers(6);
        testBoardgame2.setGenre("Family");
        testBoardgame2.setPublisher(testPublisher);

        testReservation = new Reservation();
        testReservation.setId(1L);
        testReservation.setCustomer(testUser);
        testReservation.setBoardgame(testBoardgame);
        testReservation.setReservationDate(LocalDate.now().plusDays(5));
        testReservation.setParticipantCount(4);
        testReservation.setNotes("Test reservation");

        testReservation2 = new Reservation();
        testReservation2.setId(2L);
        testReservation2.setCustomer(testUser2);
        testReservation2.setBoardgame(testBoardgame2);
        testReservation2.setReservationDate(LocalDate.now().plusDays(10));
        testReservation2.setParticipantCount(2);
        testReservation2.setNotes("Second reservation");
    }

    @Test
    void getAllReservations_ShouldReturnAllReservations() {
        List<Reservation> expectedReservations = List.of(testReservation, testReservation2);
        when(reservationRepository.findAll()).thenReturn(expectedReservations);

        List<Reservation> result = reservationService.getAllReservations();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testReservation, testReservation2);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void getReservationsByCustomer_ShouldReturnCustomerReservations() {
        List<Reservation> customerReservations = List.of(testReservation);
        when(reservationRepository.findByCustomerId(1L)).thenReturn(customerReservations);

        List<Reservation> result = reservationService.getReservationsByCustomer(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testReservation);
        verify(reservationRepository, times(1)).findByCustomerId(1L);
    }

    @Test
    void getReservationsByBoardgame_ShouldReturnBoardgameReservations() {
        List<Reservation> boardgameReservations = List.of(testReservation);
        when(reservationRepository.findByBoardgameId(1L)).thenReturn(boardgameReservations);

        List<Reservation> result = reservationService.getReservationsByBoardgame(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testReservation);
        verify(reservationRepository, times(1)).findByBoardgameId(1L);
    }

    @Test
    void createReservation_WithValidData_ShouldCreateReservation() {
        Long customerId = 1L;
        Long boardgameId = 1L;
        LocalDate reservationDate = LocalDate.now().plusDays(7);
        int participantCount = 3;
        String notes = "Test notes";

        when(userRepository.findById(customerId)).thenReturn(Optional.of(testUser));
        when(boardgameRepository.findById(boardgameId)).thenReturn(Optional.of(testBoardgame));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        Reservation result = reservationService.createReservation(customerId, boardgameId, reservationDate,
                participantCount, notes);

        assertThat(result).isEqualTo(testReservation);
        verify(userRepository, times(1)).findById(customerId);
        verify(boardgameRepository, times(1)).findById(boardgameId);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void createReservation_WithInvalidCustomerId_ShouldThrowGameNotFoundException() {
        Long invalidCustomerId = 999L;
        Long boardgameId = 1L;
        LocalDate reservationDate = LocalDate.now().plusDays(7);
        int participantCount = 3;
        String notes = "Test notes";

        when(userRepository.findById(invalidCustomerId)).thenReturn(Optional.empty());
        when(boardgameRepository.findById(boardgameId)).thenReturn(Optional.of(testBoardgame));

        assertThatThrownBy(() -> reservationService.createReservation(invalidCustomerId, boardgameId, reservationDate,
                participantCount, notes))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage("Customer with ID 999 wasn't found.");

        verify(userRepository, times(1)).findById(invalidCustomerId);
        verify(boardgameRepository, times(1)).findById(boardgameId);
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createReservation_WithInvalidBoardgameId_ShouldThrowGameNotFoundException() {
        Long customerId = 1L;
        Long invalidBoardgameId = 999L;
        LocalDate reservationDate = LocalDate.now().plusDays(7);
        int participantCount = 3;
        String notes = "Test notes";

        when(userRepository.findById(customerId)).thenReturn(Optional.of(testUser));
        when(boardgameRepository.findById(invalidBoardgameId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.createReservation(customerId, invalidBoardgameId, reservationDate,
                participantCount, notes))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage("Boardgame with ID 999 wasn't found.");

        verify(userRepository, times(1)).findById(customerId);
        verify(boardgameRepository, times(1)).findById(invalidBoardgameId);
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void cancelReservation_WithValidId_ShouldDeleteReservation() {
        when(reservationRepository.existsById(1L)).thenReturn(true);

        reservationService.cancelReservation(1L);

        verify(reservationRepository, times(1)).existsById(1L);
        verify(reservationRepository, times(1)).deleteById(1L);
    }

    @Test
    void cancelReservation_WithInvalidId_ShouldThrowGameNotFoundException() {
        when(reservationRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> reservationService.cancelReservation(999L))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage("Reservation with ID 999 wasn't found.");

        verify(reservationRepository, times(1)).existsById(999L);
        verify(reservationRepository, never()).deleteById(anyLong());
    }

    @Test
    void getTotalReservationsCount_ShouldReturnTotalCount() {
        when(reservationRepository.count()).thenReturn(5L);

        long result = reservationService.getTotalReservationsCount();

        assertThat(result).isEqualTo(5L);
        verify(reservationRepository, times(1)).count();
    }

    @Test
    void getActiveReservationsCount_ShouldReturnActiveCount() {
        Reservation pastReservation = new Reservation();
        pastReservation.setReservationDate(LocalDate.now().minusDays(1));

        Reservation futureReservation = new Reservation();
        futureReservation.setReservationDate(LocalDate.now().plusDays(1));

        List<Reservation> allReservations = List.of(pastReservation, futureReservation, testReservation);
        when(reservationRepository.findAll()).thenReturn(allReservations);

        long result = reservationService.getActiveReservationsCount();

        assertThat(result).isEqualTo(2L);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void getMonthlyReservations_ShouldReturnMonthlyStats() {
        Reservation januaryReservation = new Reservation();
        januaryReservation.setReservationDate(LocalDate.of(2024, 1, 15));
        januaryReservation.setParticipantCount(3);

        Reservation anotherJanuaryReservation = new Reservation();
        anotherJanuaryReservation.setReservationDate(LocalDate.of(2024, 1, 25));
        anotherJanuaryReservation.setParticipantCount(5);

        Reservation februaryReservation = new Reservation();
        februaryReservation.setReservationDate(LocalDate.of(2024, 2, 10));
        februaryReservation.setParticipantCount(2);

        List<Reservation> allReservations = List.of(januaryReservation, anotherJanuaryReservation, februaryReservation);
        when(reservationRepository.findAll()).thenReturn(allReservations);

        Map<String, Object> result = reservationService.getMonthlyReservations(2024, 1);

        assertThat(result.get("year")).isEqualTo(2024);
        assertThat(result.get("month")).isEqualTo(1);
        assertThat(result.get("totalReservations")).isEqualTo(2);
        assertThat(result.get("totalParticipants")).isEqualTo(8);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void getMonthlyReservations_WithNoReservationsInMonth_ShouldReturnZeroStats() {
        List<Reservation> allReservations = List.of();
        when(reservationRepository.findAll()).thenReturn(allReservations);

        Map<String, Object> result = reservationService.getMonthlyReservations(2024, 3);

        assertThat(result.get("year")).isEqualTo(2024);
        assertThat(result.get("month")).isEqualTo(3);
        assertThat(result.get("totalReservations")).isEqualTo(0);
        assertThat(result.get("totalParticipants")).isEqualTo(0);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void getMonthlyReservations_WithReservationsOutsideMonth_ShouldFilterCorrectly() {
        Reservation beforeMonth = new Reservation();
        beforeMonth.setReservationDate(LocalDate.of(2024, 1, 31));
        beforeMonth.setParticipantCount(3);

        Reservation inMonth = new Reservation();
        inMonth.setReservationDate(LocalDate.of(2024, 2, 15));
        inMonth.setParticipantCount(4);

        Reservation afterMonth = new Reservation();
        afterMonth.setReservationDate(LocalDate.of(2024, 3, 1));
        afterMonth.setParticipantCount(2);

        List<Reservation> allReservations = List.of(beforeMonth, inMonth, afterMonth);
        when(reservationRepository.findAll()).thenReturn(allReservations);

        Map<String, Object> result = reservationService.getMonthlyReservations(2024, 2);

        assertThat(result.get("year")).isEqualTo(2024);
        assertThat(result.get("month")).isEqualTo(2);
        assertThat(result.get("totalReservations")).isEqualTo(1);
        assertThat(result.get("totalParticipants")).isEqualTo(4);
        verify(reservationRepository, times(1)).findAll();
    }
}