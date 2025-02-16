package com.meepleconnect.boardgamesapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.models.Reservation;
import com.meepleconnect.boardgamesapi.models.User;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private BoardgameRepository boardgameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Boardgame testGame;
    private Reservation testReservation;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        boardgameRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User("testUser", "password", null);
        testGame = new Boardgame("Catan", 39.99, 2, true, 3, 4, "Strategy", null);
        userRepository.save(testUser);
        boardgameRepository.save(testGame);

        testReservation = new Reservation(testUser, testGame, LocalDate.now(), 4, "Test notes");
        reservationRepository.save(testReservation);
    }

    @Test
    void getAllReservations_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].notes").value("Test notes"));
    }

    @Test
    void createReservation_ShouldReturnCreatedReservation() throws Exception {
        mockMvc.perform(post("/api/reservations")
                        .param("customerId", testUser.getId().toString())
                        .param("boardgameId", testGame.getId().toString())
                        .param("reservationDate", LocalDate.now().toString())
                        .param("participantCount", "4")
                        .param("notes", "New reservation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes").value("New reservation"));
    }
}
