package com.meepleconnect.boardgamesapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleconnect.boardgamesapi.entities.User;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.models.Reservation;
import com.meepleconnect.boardgamesapi.repositories.BoardgameRepository;
import com.meepleconnect.boardgamesapi.repositories.ReservationRepository;
import com.meepleconnect.boardgamesapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardgameRepository boardgameRepository;

    private User testUser;
    private Boardgame testBoardgame;
    private Reservation testReservation;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setUserName("testuser");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);

        // Create test boardgame
        testBoardgame = new Boardgame("Test Game", new BigDecimal("29.99"), true, 2, 4, "Strategy", null);
        testBoardgame = boardgameRepository.save(testBoardgame);

        // Create test reservation
        testReservation = new Reservation(testUser, testBoardgame, LocalDate.now().plusDays(1), 3, "Test reservation");
        testReservation = reservationRepository.save(testReservation);
    }

    @Test
    void getAllReservations_ShouldReturnReservations() throws Exception {
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void getReservationById_ExistingId_ShouldReturnReservation() throws Exception {
        mockMvc.perform(get("/api/reservations/" + testReservation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testReservation.getId()))
                .andExpect(jsonPath("$.participantCount").value(3));
    }

    @Test
    void getReservationById_NonExistingId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/reservations/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createReservation_ValidData_ShouldCreateReservation() throws Exception {
        ReservationRequest request = new ReservationRequest(
                testUser.getId(),
                testBoardgame.getId(),
                LocalDate.now().plusDays(2),
                4,
                "New reservation"
        );

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.participantCount").value(4))
                .andExpect(jsonPath("$.notes").value("New reservation"));
    }

    @Test
    void deleteReservation_ExistingId_ShouldDeleteReservation() throws Exception {
        mockMvc.perform(delete("/api/reservations/" + testReservation.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteReservation_NonExistingId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/reservations/999"))
                .andExpect(status().isNotFound());
    }

    // Helper class for reservation requests
    private static class ReservationRequest {
        private Long customerId;
        private Long boardgameId;
        private LocalDate reservationDate;
        private int participantCount;
        private String notes;

        public ReservationRequest(Long customerId, Long boardgameId, LocalDate reservationDate, int participantCount, String notes) {
            this.customerId = customerId;
            this.boardgameId = boardgameId;
            this.reservationDate = reservationDate;
            this.participantCount = participantCount;
            this.notes = notes;
        }

        // Getters and setters
        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }
        public Long getBoardgameId() { return boardgameId; }
        public void setBoardgameId(Long boardgameId) { this.boardgameId = boardgameId; }
        public LocalDate getReservationDate() { return reservationDate; }
        public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }
        public int getParticipantCount() { return participantCount; }
        public void setParticipantCount(int participantCount) { this.participantCount = participantCount; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}
