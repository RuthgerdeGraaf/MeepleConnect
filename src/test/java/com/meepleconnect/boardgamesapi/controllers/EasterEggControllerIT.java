package com.meepleconnect.boardgamesapi.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
public class EasterEggControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getTeapot_ShouldReturn418StatusAndTeapotMessage() throws Exception {
        mockMvc.perform(get("/api/fun/teapot"))
                .andExpect(status().isIAmATeapot())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("I'm a Teapot"))
                .andExpect(jsonPath("$.message").value("I'm a teapot! 🍵 This server refuses to brew coffee."));
    }

    @Test
    void getBoardgameTeapot_WithId418_ShouldReturn418Status() throws Exception {
        mockMvc.perform(get("/api/fun/boardgames/418"))
                .andExpect(status().isIAmATeapot())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("I'm a Teapot"))
                .andExpect(jsonPath("$.message").value("This boardgame is a teapot!"));
    }

    @Test
    void getBoardgameTeapot_WithId1_ShouldReturn404Status() throws Exception {
        mockMvc.perform(get("/api/fun/boardgames/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Game Not Found"))
                .andExpect(jsonPath("$.message").value("Boardgame with ID 1 not found."));
    }

    @Test
    void getBoardgameTeapot_WithId999_ShouldReturn404Status() throws Exception {
        mockMvc.perform(get("/api/fun/boardgames/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Game Not Found"))
                .andExpect(jsonPath("$.message").value("Boardgame with ID 999 not found."));
    }

    @Test
    void getBoardgameTeapot_WithId0_ShouldReturn404Status() throws Exception {
        mockMvc.perform(get("/api/fun/boardgames/0"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Game Not Found"))
                .andExpect(jsonPath("$.message").value("Boardgame with ID 0 not found."));
    }

    @Test
    void getBoardgameTeapot_WithNegativeId_ShouldReturn404Status() throws Exception {
        mockMvc.perform(get("/api/fun/boardgames/-1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Game Not Found"))
                .andExpect(jsonPath("$.message").value("Boardgame with ID -1 not found."));
    }

    @Test
    void getBoardgameTeapot_WithLargeId_ShouldReturn404Status() throws Exception {
        mockMvc.perform(get("/api/fun/boardgames/999999999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Game Not Found"))
                .andExpect(jsonPath("$.message").value("Boardgame with ID 999999999 not found."));
    }

    @Test
    void getBoardgameTeapot_WithId417_ShouldReturn404Status() throws Exception {
        mockMvc.perform(get("/api/fun/boardgames/417"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Game Not Found"))
                .andExpect(jsonPath("$.message").value("Boardgame with ID 417 not found."));
    }

    @Test
    void getBoardgameTeapot_WithId419_ShouldReturn404Status() throws Exception {
        mockMvc.perform(get("/api/fun/boardgames/419"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Game Not Found"))
                .andExpect(jsonPath("$.message").value("Boardgame with ID 419 not found."));
    }
}