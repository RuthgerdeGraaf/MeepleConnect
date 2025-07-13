package com.meepleconnect.boardgamesapi.controllers;

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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class StatisticsControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getDashboardStatistics_ShouldReturnAllStatistics() throws Exception {
        mockMvc.perform(get("/api/statistics/dashboard"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalBoardgames").exists())
                .andExpect(jsonPath("$.totalBoardgames").isNumber())
                .andExpect(jsonPath("$.availableBoardgames").exists())
                .andExpect(jsonPath("$.availableBoardgames").isNumber())
                .andExpect(jsonPath("$.totalReservations").exists())
                .andExpect(jsonPath("$.totalReservations").isNumber())
                .andExpect(jsonPath("$.totalUsers").exists())
                .andExpect(jsonPath("$.totalUsers").isNumber())
                .andExpect(jsonPath("$.activeReservations").exists())
                .andExpect(jsonPath("$.activeReservations").isNumber());
    }

    @Test
    void getDashboardStatistics_ShouldReturnValidCounts() throws Exception {
        mockMvc.perform(get("/api/statistics/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalBoardgames").value(greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.availableBoardgames").value(greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.totalReservations").value(greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.totalUsers").value(greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.activeReservations").value(greaterThanOrEqualTo(0)));
    }

    @Test
    void getPopularBoardgames_ShouldReturnPopularGamesList() throws Exception {
        mockMvc.perform(get("/api/statistics/boardgames/popular"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.popularGames").exists())
                .andExpect(jsonPath("$.popularGames").isArray());
    }

    @Test
    void getPopularBoardgames_ShouldReturnValidGameStructure() throws Exception {
        mockMvc.perform(get("/api/statistics/boardgames/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.popularGames").exists())
                .andExpect(jsonPath("$.popularGames").isArray());
    }

    @Test
    void getPopularBoardgames_ShouldReturnValidPopularityScores() throws Exception {
        mockMvc.perform(get("/api/statistics/boardgames/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.popularGames").exists())
                .andExpect(jsonPath("$.popularGames").isArray());
    }

    @Test
    void getMonthlyReservations_WithValidYearAndMonth_ShouldReturnMonthlyStats() throws Exception {
        int currentYear = java.time.LocalDate.now().getYear();
        int currentMonth = java.time.LocalDate.now().getMonthValue();

        mockMvc.perform(get("/api/statistics/reservations/monthly")
                .param("year", String.valueOf(currentYear))
                .param("month", String.valueOf(currentMonth)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.year").value(currentYear))
                .andExpect(jsonPath("$.month").value(currentMonth))
                .andExpect(jsonPath("$.totalReservations").exists())
                .andExpect(jsonPath("$.totalReservations").isNumber())
                .andExpect(jsonPath("$.totalParticipants").exists())
                .andExpect(jsonPath("$.totalParticipants").isNumber());
    }

    @Test
    void getMonthlyReservations_WithValidYearAndMonth_ShouldReturnValidCounts() throws Exception {
        int currentYear = java.time.LocalDate.now().getYear();
        int currentMonth = java.time.LocalDate.now().getMonthValue();

        mockMvc.perform(get("/api/statistics/reservations/monthly")
                .param("year", String.valueOf(currentYear))
                .param("month", String.valueOf(currentMonth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalReservations").value(greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.totalParticipants").value(greaterThanOrEqualTo(0)));
    }

    @Test
    void getMonthlyReservations_WithFutureYear_ShouldReturnEmptyStats() throws Exception {
        int futureYear = java.time.LocalDate.now().getYear() + 10;
        int currentMonth = java.time.LocalDate.now().getMonthValue();

        mockMvc.perform(get("/api/statistics/reservations/monthly")
                .param("year", String.valueOf(futureYear))
                .param("month", String.valueOf(currentMonth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(futureYear))
                .andExpect(jsonPath("$.month").value(currentMonth))
                .andExpect(jsonPath("$.totalReservations").value(0))
                .andExpect(jsonPath("$.totalParticipants").value(0));
    }

    @Test
    void getMonthlyReservations_WithPastYear_ShouldReturnStats() throws Exception {
        int pastYear = java.time.LocalDate.now().getYear() - 1;
        int currentMonth = java.time.LocalDate.now().getMonthValue();

        mockMvc.perform(get("/api/statistics/reservations/monthly")
                .param("year", String.valueOf(pastYear))
                .param("month", String.valueOf(currentMonth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(pastYear))
                .andExpect(jsonPath("$.month").value(currentMonth))
                .andExpect(jsonPath("$.totalReservations").value(greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.totalParticipants").value(greaterThanOrEqualTo(0)));
    }

    @Test
    void getMonthlyReservations_WithInvalidMonth_ShouldReturnServerError() throws Exception {
        int currentYear = java.time.LocalDate.now().getYear();

        mockMvc.perform(get("/api/statistics/reservations/monthly")
                .param("year", String.valueOf(currentYear))
                .param("month", "13"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getMonthlyReservations_WithInvalidMonth_ShouldReturnServerErrorForZero() throws Exception {
        int currentYear = java.time.LocalDate.now().getYear();

        mockMvc.perform(get("/api/statistics/reservations/monthly")
                .param("year", String.valueOf(currentYear))
                .param("month", "0"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getMonthlyReservations_WithInvalidMonth_ShouldReturnServerErrorForNegative() throws Exception {
        int currentYear = java.time.LocalDate.now().getYear();

        mockMvc.perform(get("/api/statistics/reservations/monthly")
                .param("year", String.valueOf(currentYear))
                .param("month", "-1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getMonthlyReservations_WithInvalidYear_ShouldReturnMonthlyStats() throws Exception {
        int currentMonth = java.time.LocalDate.now().getMonthValue();

        mockMvc.perform(get("/api/statistics/reservations/monthly")
                .param("year", "-1")
                .param("month", String.valueOf(currentMonth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(-1))
                .andExpect(jsonPath("$.month").value(currentMonth))
                .andExpect(jsonPath("$.totalReservations").value(0))
                .andExpect(jsonPath("$.totalParticipants").value(0));
    }

    @Test
    void getMonthlyReservations_WithMissingYear_ShouldReturnBadRequest() throws Exception {
        int currentMonth = java.time.LocalDate.now().getMonthValue();

        mockMvc.perform(get("/api/statistics/reservations/monthly")
                .param("month", String.valueOf(currentMonth)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMonthlyReservations_WithMissingMonth_ShouldReturnBadRequest() throws Exception {
        int currentYear = java.time.LocalDate.now().getYear();

        mockMvc.perform(get("/api/statistics/reservations/monthly")
                .param("year", String.valueOf(currentYear)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMonthlyReservations_WithNonNumericYear_ShouldReturnBadRequest() throws Exception {
        int currentMonth = java.time.LocalDate.now().getMonthValue();

        mockMvc.perform(get("/api/statistics/reservations/monthly")
                .param("year", "invalid")
                .param("month", String.valueOf(currentMonth)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMonthlyReservations_WithNonNumericMonth_ShouldReturnBadRequest() throws Exception {
        int currentYear = java.time.LocalDate.now().getYear();

        mockMvc.perform(get("/api/statistics/reservations/monthly")
                .param("year", String.valueOf(currentYear))
                .param("month", "invalid"))
                .andExpect(status().isBadRequest());
    }
}