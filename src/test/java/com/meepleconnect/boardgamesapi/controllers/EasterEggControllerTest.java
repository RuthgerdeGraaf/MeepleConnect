package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.exceptions.TeapotException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EasterEggControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void getTeapot_ShouldReturn418() throws Exception {
        mockMvc.perform(get("/api/fun/teapot"))
                .andExpect(status().isIAmATeapot())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TeapotException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains("I'm a teapot")));
    }

    @Test
    @WithMockUser
    void getBoardgameTeapot_With418_ShouldReturn418() throws Exception {
        mockMvc.perform(get("/api/fun/boardgames/418"))
                .andExpect(status().isIAmATeapot())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TeapotException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains("This boardgame is a teapot!")));
    }

    @Test
    @WithMockUser
    void getBoardgameTeapot_WithOtherId_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/fun/boardgames/123"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty()));
    }
} 