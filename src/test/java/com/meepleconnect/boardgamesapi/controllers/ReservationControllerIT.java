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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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
        testGame = new Boardgame("Catan", BigDecimal.valueOf(39.99), 2, true, 3, 4, "Strategy", null);
        userRepository.save(testUser);
        boardgameRepository.save(testGame);

        testReservation = new Reservation(testUser, testGame, LocalDate.now().plusDays(5), 4, "Game night!");
        reservationRepository.save(testReservation);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getAllReservations_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].notes").value("Game night!"));
    }

    @Test
    void getAllReservations_Unauthorized_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getReservationsByCustomer_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/reservations/customer/" + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].notes").value("Game night!"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void createReservation_ShouldCreateReservation() throws Exception {
        Reservation newReservation = new Reservation(testUser, testGame, LocalDate.now().plusDays(7), 3, "Another game night");

        mockMvc.perform(post("/api/reservations")
                        .param("customerId", testUser.getId().toString())
                        .param("boardgameId", testGame.getId().toString())
                        .param("reservationDate", newReservation.getReservationDate().toString())
                        .param("participantCount", String.valueOf(newReservation.getParticipantCount()))
                        .param("notes", newReservation.getNotes()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes").value("Another game night"));
    }

    @Test
    void createReservation_Unauthorized_ShouldReturn403() throws Exception {
        mockMvc.perform(post("/api/reservations")
                        .param("customerId", testUser.getId().toString())
                        .param("boardgameId", testGame.getId().toString())
                        .param("reservationDate", LocalDate.now().plusDays(7).toString())
                        .param("participantCount", "3")
                        .param("notes", "No access"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void cancelReservation_ShouldDeleteReservation() throws Exception {
        mockMvc.perform(delete("/api/reservations/" + testReservation.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void cancelReservation_NonExisting_ShouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/reservations/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cancelReservation_Unauthorized_ShouldReturn403() throws Exception {
        mockMvc.perform(delete("/api/reservations/" + testReservation.getId()))
                .andExpect(status().isForbidden());
    }
}
