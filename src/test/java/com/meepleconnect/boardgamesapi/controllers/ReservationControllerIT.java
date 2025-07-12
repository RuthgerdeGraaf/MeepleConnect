package com.meepleconnect.boardgamesapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleconnect.boardgamesapi.dtos.ReservationRequestDTO;
import com.meepleconnect.boardgamesapi.entities.User;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.models.Publisher;
import com.meepleconnect.boardgamesapi.repositories.BoardgameRepository;
import com.meepleconnect.boardgamesapi.repositories.PublisherRepository;
import com.meepleconnect.boardgamesapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class ReservationControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardgameRepository boardgameRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        createTestData();
    }

    private void createTestData() {
        if (userRepository.findByUserName("testuser").isPresent() && 
            boardgameRepository.findByNameIgnoreCase("Test Boardgame").isPresent()) {
            return;
        }

        Publisher publisher = new Publisher();
        publisher.setName("Test Publisher");
        publisher.setCountryOfOrigin("Netherlands");
        publisher.setFounded(2020);
        publisher.setIndie(true);
        publisher = publisherRepository.save(publisher);

        User user = new User();
        user.setUserName("testuser");
        user.setPassword("$2a$10$bJxwWc3A3DBzke7Gnb/MZ.lLXmvOIE/DFAd6QUnBvWhn7c7D1zY4C");
        user.setEnabled(true);
        user.setExpired(false);
        user.setLocked(false);
        user.setAreCredentialsExpired(false);
        user = userRepository.save(user);

        Boardgame boardgame = new Boardgame();
        boardgame.setName("Test Boardgame");
        boardgame.setPrice(new java.math.BigDecimal("29.99"));
        boardgame.setMinPlayers(2);
        boardgame.setMaxPlayers(4);
        boardgame.setGenre("Strategy");
        boardgame.setAvailable(true);
        boardgame.setPublisher(publisher);
        boardgameRepository.save(boardgame);
    }

    @Test
    void getAllReservations_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void getReservationsByCustomer_ShouldReturnList() throws Exception {
        User testUser = userRepository.findByUserName("testuser").orElseThrow();
        
        mockMvc.perform(get("/api/reservations/customer/" + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void getReservationsByBoardgame_ShouldReturnList() throws Exception {
        Boardgame testBoardgame = boardgameRepository.findByNameIgnoreCase("Test Boardgame").orElseThrow();
        
        mockMvc.perform(get("/api/reservations/boardgame/" + testBoardgame.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void createReservation_ShouldReturnCreatedReservation() throws Exception {
        User testUser = userRepository.findByUserName("testuser").orElseThrow();
        Boardgame testBoardgame = boardgameRepository.findByNameIgnoreCase("Test Boardgame").orElseThrow();

        ReservationRequestDTO dto = new ReservationRequestDTO();
        dto.setCustomerId(testUser.getId());
        dto.setBoardgameId(testBoardgame.getId());
        dto.setReservationDate(LocalDate.now().plusDays(2));
        dto.setParticipantCount(2);
        dto.setNotes("Test reservering");

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.reservationDate").value(dto.getReservationDate().toString()))
                .andExpect(jsonPath("$.participantCount").value(2))
                .andExpect(header().string("Location", containsString("/api/reservations/")));
    }

    @Test
    void cancelReservation_ShouldReturnNoContent() throws Exception {
        User testUser = userRepository.findByUserName("testuser").orElseThrow();
        Boardgame testBoardgame = boardgameRepository.findByNameIgnoreCase("Test Boardgame").orElseThrow();

        ReservationRequestDTO dto = new ReservationRequestDTO();
        dto.setCustomerId(testUser.getId());
        dto.setBoardgameId(testBoardgame.getId());
        dto.setReservationDate(LocalDate.now().plusDays(3));
        dto.setParticipantCount(2);
        dto.setNotes("Voor annulering");

        String location = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        String reservationId = location.substring(location.lastIndexOf("/") + 1);

        mockMvc.perform(delete("/api/reservations/" + reservationId))
                .andExpect(status().isNoContent());
    }
}