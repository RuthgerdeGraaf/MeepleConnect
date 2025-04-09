package com.meepleconnect.boardgamesapi.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.repositories.BoardgameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardGameControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardgameRepository boardgameRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Boardgame testGame;

    @BeforeEach
    void setUp() {
        boardgameRepository.deleteAll();
        testGame = new Boardgame("Catan", new BigDecimal("39.99"), true, 3, 4, "Strategy", null);
        boardgameRepository.save(testGame);
    }

    @Test
    @WithMockUser
    void getAllBoardgames_ShouldReturnList() throws Exception {
        String jsonResponse = mockMvc.perform(get("/api/boardgames"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Boardgame> boardgames = objectMapper.readValue(jsonResponse, new TypeReference<List<Boardgame>>() {});

        assertFalse(boardgames.isEmpty());
        assertEquals("Catan", boardgames.get(0).getName());
    }

    @Test
    @WithMockUser
    void getBoardgameById_ShouldReturnGame() throws Exception {
        mockMvc.perform(get("/api/boardgames/" + testGame.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Catan"));
    }

    @Test
    @WithMockUser
    void getBoardgameById_NonExisting_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/boardgames/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void addBoardgame_ShouldCreateGame() throws Exception {
        Boardgame newGame = new Boardgame("Terraforming Mars", new BigDecimal("59.99"), true, 1, 5, "Strategy", null);

        mockMvc.perform(post("/api/boardgames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGame)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Terraforming Mars"));
    }

    @Test
    void addBoardgame_Unauthorized_ShouldReturn403() throws Exception {
        Boardgame newGame = new Boardgame("Terraforming Mars", new BigDecimal("59.99"), true, 1, 5, "Strategy", null);

        mockMvc.perform(post("/api/boardgames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGame)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void updateBoardgame_ShouldUpdateGame() throws Exception {
        Boardgame updatedGame = new Boardgame("Updated Catan", new BigDecimal("49.99"), true, 2, 5, "Adventure", null);

        mockMvc.perform(put("/api/boardgames/" + testGame.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedGame)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Catan"));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void updateBoardgame_NonExisting_ShouldReturn404() throws Exception {
        Boardgame updatedGame = new Boardgame("Updated Catan", new BigDecimal("49.99"), true, 2, 5, "Adventure", null);

        mockMvc.perform(put("/api/boardgames/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedGame)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void deleteBoardgame_ShouldDeleteGame() throws Exception {
        mockMvc.perform(delete("/api/boardgames/" + testGame.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void deleteBoardgame_NonExisting_ShouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/boardgames/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBoardgame_Unauthorized_ShouldReturn403() throws Exception {
        mockMvc.perform(delete("/api/boardgames/" + testGame.getId()))
                .andExpect(status().isForbidden());
    }

//    @Test
//    @WithMockUser
//    void getSpecialBoardgame_ShouldReturnTeapotException() throws Exception {
//        mockMvc.perform(get("/api/fun/boardgames/418"))
//            .andExpect(status().isIAmATeapot())
//            .andExpect(jsonPath("$.error").value("I'm a teapot"))
//            .andExpect(jsonPath("$.message").value("Dit bordspel is een theepot!"));
//    }

    @Test
    @WithMockUser
    void getBoardgamesByGenre_ShouldReturnFilteredResults() throws Exception {
        Boardgame strategyGame = new Boardgame("Terraforming Mars", new BigDecimal("59.99"), true, 1, 5, "Strategy", null);
        Boardgame adventureGame = new Boardgame("Gloomhaven", new BigDecimal("89.99"), true, 1, 4, "Adventure", null);
        boardgameRepository.save(strategyGame);
        boardgameRepository.save(adventureGame);

        String jsonResponse = mockMvc.perform(get("/api/boardgames?genre=Strategy"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<Boardgame> filteredBoardgames = objectMapper.readValue(jsonResponse, new TypeReference<List<Boardgame>>() {});

        assertFalse(filteredBoardgames.isEmpty());
        assertEquals(2, filteredBoardgames.size());
        assertEquals("Catan", filteredBoardgames.get(0).getName());
        assertEquals("Terraforming Mars", filteredBoardgames.get(1).getName());
    }

    @Test
    @WithMockUser
    void getBoardgamesByAvailability_ShouldReturnFilteredResults() throws Exception {
        Boardgame availableGame = new Boardgame("Terraforming Mars", new BigDecimal("59.99"), true, 1, 5, "Strategy", null);
        Boardgame unavailableGame = new Boardgame("Gloomhaven", new BigDecimal("89.99"), false, 1, 4, "Adventure", null);
        boardgameRepository.save(availableGame);
        boardgameRepository.save(unavailableGame);

        String jsonResponse = mockMvc.perform(get("/api/boardgames?available=true"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<Boardgame> filteredBoardgames = objectMapper.readValue(jsonResponse, new TypeReference<List<Boardgame>>() {});

        assertFalse(filteredBoardgames.isEmpty());
        assertEquals(2, filteredBoardgames.size());
        assertTrue(filteredBoardgames.stream().allMatch(Boardgame::isAvailable));
    }

    @Test
    @WithMockUser
    void getBoardgamesByMinPlayers_ShouldReturnFilteredResults() throws Exception {
        Boardgame twoPlayerGame = new Boardgame("7 Wonders Duel", new BigDecimal("39.99"), true, 2, 2, "Strategy", null);
        Boardgame threePlayerGame = new Boardgame("Carcassonne", new BigDecimal("29.99"), true, 2, 5, "Strategy", null);
        boardgameRepository.save(twoPlayerGame);
        boardgameRepository.save(threePlayerGame);

        String jsonResponse = mockMvc.perform(get("/api/boardgames?minPlayers=3"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<Boardgame> filteredBoardgames = objectMapper.readValue(jsonResponse, new TypeReference<List<Boardgame>>() {});

        assertFalse(filteredBoardgames.isEmpty());
        assertEquals(1, filteredBoardgames.size());
        assertEquals("Catan", filteredBoardgames.get(0).getName());
    }

    @Test
    @WithMockUser
    void getBoardgamesByMaxPlayers_ShouldReturnFilteredResults() throws Exception {
        Boardgame fourPlayerGame = new Boardgame("Ticket to Ride", new BigDecimal("49.99"), true, 2, 4, "Family", null);
        Boardgame fivePlayerGame = new Boardgame("Pandemic", new BigDecimal("44.99"), true, 2, 4, "Cooperative", null);
        boardgameRepository.save(fourPlayerGame);
        boardgameRepository.save(fivePlayerGame);

        String jsonResponse = mockMvc.perform(get("/api/boardgames?maxPlayers=4"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<Boardgame> filteredBoardgames = objectMapper.readValue(jsonResponse, new TypeReference<List<Boardgame>>() {});

        assertFalse(filteredBoardgames.isEmpty());
        assertEquals(3, filteredBoardgames.size());
        assertTrue(filteredBoardgames.stream().allMatch(game -> game.getMaxPlayers() <= 4));
    }

    @Test
    @WithMockUser
    void getBoardgamesByMultipleFilters_ShouldReturnFilteredResults() throws Exception {
        Boardgame strategyGame = new Boardgame("Terraforming Mars", new BigDecimal("59.99"), true, 1, 5, "Strategy", null);
        Boardgame adventureGame = new Boardgame("Gloomhaven", new BigDecimal("89.99"), false, 1, 4, "Adventure", null);
        boardgameRepository.save(strategyGame);
        boardgameRepository.save(adventureGame);

        String jsonResponse = mockMvc.perform(get("/api/boardgames?genre=Strategy&available=true&minPlayers=2&maxPlayers=4"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<Boardgame> filteredBoardgames = objectMapper.readValue(jsonResponse, new TypeReference<List<Boardgame>>() {});

        assertFalse(filteredBoardgames.isEmpty());
        assertEquals(1, filteredBoardgames.size());
        assertEquals("Catan", filteredBoardgames.get(0).getName());
    }
}
