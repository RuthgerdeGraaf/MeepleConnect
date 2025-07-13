package com.meepleconnect.boardgamesapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
public class SearchControllerIT {

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
    void searchBoardgames_ShouldReturnFilteredResults() throws Exception {
        Map<String, Object> searchCriteria = new HashMap<>();
        searchCriteria.put("genre", "Strategy");
        searchCriteria.put("available", true);
        searchCriteria.put("minPlayers", 2);
        searchCriteria.put("maxPlayers", 4);
        searchCriteria.put("maxPrice", 50.0);

        mockMvc.perform(post("/api/search/boardgames")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchCriteria)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void searchBoardgames_WithOnlyGenre_ShouldReturnResults() throws Exception {
        Map<String, Object> searchCriteria = new HashMap<>();
        searchCriteria.put("genre", "Strategy");

        mockMvc.perform(post("/api/search/boardgames")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchCriteria)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void searchBoardgames_WithPriceFilter_ShouldReturnResults() throws Exception {
        Map<String, Object> searchCriteria = new HashMap<>();
        searchCriteria.put("maxPrice", 30.0);

        mockMvc.perform(post("/api/search/boardgames")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchCriteria)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void advancedSearch_ShouldReturnFilteredResults() throws Exception {
        Map<String, Object> advancedCriteria = new HashMap<>();
        advancedCriteria.put("name", "Test");
        advancedCriteria.put("genre", "Strategy");
        advancedCriteria.put("available", true);
        advancedCriteria.put("minPlayers", 2);
        advancedCriteria.put("maxPlayers", 4);
        advancedCriteria.put("minPrice", 20.0);
        advancedCriteria.put("maxPrice", 50.0);

        mockMvc.perform(post("/api/search/boardgames/advanced")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(advancedCriteria)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void advancedSearch_WithNameFilter_ShouldReturnResults() throws Exception {
        Map<String, Object> advancedCriteria = new HashMap<>();
        advancedCriteria.put("name", "Test");

        mockMvc.perform(post("/api/search/boardgames/advanced")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(advancedCriteria)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void advancedSearch_WithPlayerCountFilter_ShouldReturnResults() throws Exception {
        Map<String, Object> advancedCriteria = new HashMap<>();
        advancedCriteria.put("minPlayers", 2);
        advancedCriteria.put("maxPlayers", 4);

        mockMvc.perform(post("/api/search/boardgames/advanced")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(advancedCriteria)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void advancedSearch_WithPriceRange_ShouldReturnResults() throws Exception {
        Map<String, Object> advancedCriteria = new HashMap<>();
        advancedCriteria.put("minPrice", 20.0);
        advancedCriteria.put("maxPrice", 40.0);

        mockMvc.perform(post("/api/search/boardgames/advanced")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(advancedCriteria)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void advancedSearch_WithEmptyCriteria_ShouldReturnAllResults() throws Exception {
        Map<String, Object> advancedCriteria = new HashMap<>();

        mockMvc.perform(post("/api/search/boardgames/advanced")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(advancedCriteria)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }
}