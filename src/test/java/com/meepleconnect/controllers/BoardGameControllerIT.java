package com.meepleconnect.boardgamesapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.repositories.BoardgameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class BoardgameControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardgameRepository boardgameRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Boardgame testGame;

    @BeforeEach
    void setUp() {
        boardgameRepository.deleteAll(); // Leeg de database voor elke test
        testGame = new Boardgame("Catan", 39.99, 2, true, 3, 4, "Strategy", null);
        boardgameRepository.save(testGame);
    }

    @Test
    void getAllBoardgames_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/boardgames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Catan"));
    }

    @Test
    void addBoardgame_ShouldReturnCreatedBoardgame() throws Exception {
        Boardgame newGame = new Boardgame("Terraforming Mars", 59.99, 3, true, 1, 5, "Strategy", null);

        mockMvc.perform(post("/api/boardgames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGame)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Terraforming Mars"));
    }
}
