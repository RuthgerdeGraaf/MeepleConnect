package com.meepleconnect.boardgamesapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ReviewControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getAllReviews_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].boardgameName").value("Catan"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("Edwin"));
    }

    @Test
    void getReviewById_ShouldReturnReview() throws Exception {
        mockMvc.perform(get("/api/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.boardgameName").value("Catan"))
                .andExpect(jsonPath("$.username").value("Ruthger"))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.helpful").value(12));
    }

    @Test
    void getReviewsByBoardgame_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/reviews/boardgame/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].boardgameId").value(1))
                .andExpect(jsonPath("$[0].boardgameName").value("Catan"));
    }

    @Test
    void createReview_ShouldReturnCreatedReview() throws Exception {
        Map<String, Object> reviewRequest = new HashMap<>();
        reviewRequest.put("boardgameId", 1L);
        reviewRequest.put("userId", 1L);
        reviewRequest.put("rating", 4);
        reviewRequest.put("comment", "Goed spel, aan te raden!");

        mockMvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.boardgameId").value(1))
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.comment").value("Goed spel, aan te raden!"))
                .andExpect(jsonPath("$.helpful").value(0));
    }

    @Test
    void updateReview_ShouldReturnUpdatedReview() throws Exception {
        Map<String, Object> reviewUpdate = new HashMap<>();
        reviewUpdate.put("rating", 3);
        reviewUpdate.put("comment", "Aangepaste review comment");

        mockMvc.perform(put("/api/reviews/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rating").value(3))
                .andExpect(jsonPath("$.comment").value("Aangepaste review comment"))
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    void deleteReview_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAverageRating_ShouldReturnRatingStats() throws Exception {
        mockMvc.perform(get("/api/reviews/boardgame/1/average-rating"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.boardgameId").value(1))
                .andExpect(jsonPath("$.boardgameName").value("Catan"))
                .andExpect(jsonPath("$.averageRating").value(4.5))
                .andExpect(jsonPath("$.totalReviews").value(25))
                .andExpect(jsonPath("$.ratingDistribution.5_stars").value(12))
                .andExpect(jsonPath("$.ratingDistribution.4_stars").value(8));
    }

    @Test
    void markAsHelpful_ShouldReturnUpdatedHelpfulCount() throws Exception {
        mockMvc.perform(post("/api/reviews/1/helpful"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.helpfulCount").value(13))
                .andExpect(jsonPath("$.markedAt").exists());
    }
}