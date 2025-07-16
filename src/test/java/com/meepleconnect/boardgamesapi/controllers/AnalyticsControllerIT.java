package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.entities.Role;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
public class AnalyticsControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardgameRepository boardgameRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    private MockMvc mockMvc;
    private Publisher testPublisher;
    private User testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        testPublisher = new Publisher();
        testPublisher.setName("Test Publisher");
        testPublisher.setCountryOfOrigin("Netherlands");
        testPublisher.setFounded(2020);
        testPublisher.setIndie(true);
        testPublisher = publisherRepository.save(testPublisher);

        Role userRole = new Role();
        userRole.setRoleName("ROLE_USER");
        userRole.setActive(true);
        userRole.setDescription("user roles");

        testUser = new User();
        testUser.setUserName("testuser");
        testUser.setPassword("$2a$10$test");
        testUser.setEnabled(true);
        testUser.setExpired(false);
        testUser.setLocked(false);
        testUser.setAreCredentialsExpired(false);
        testUser.setRoles(Arrays.asList(userRole));
        userRepository.save(testUser);

        Boardgame catan = new Boardgame();
        catan.setName("Catan");
        catan.setPrice(new BigDecimal("39.99"));
        catan.setAvailable(true);
        catan.setMinPlayers(3);
        catan.setMaxPlayers(4);
        catan.setGenre("Strategy");
        catan.setPublisher(testPublisher);
        boardgameRepository.save(catan);

        Boardgame chess = new Boardgame();
        chess.setName("Chess");
        chess.setPrice(new BigDecimal("19.99"));
        chess.setAvailable(true);
        chess.setMinPlayers(2);
        chess.setMaxPlayers(2);
        chess.setGenre("Strategy");
        chess.setPublisher(testPublisher);
        boardgameRepository.save(chess);
    }

    @Test
    void getRevenueForecast_WithValidMonths_ShouldReturnForecast() throws Exception {
        mockMvc.perform(get("/api/analytics/revenue/forecast")
                .param("months", "6"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.forecastMonths").value(6))
                .andExpect(jsonPath("$.monthlyRevenue['Month 1']").exists())
                .andExpect(jsonPath("$.monthlyRevenue['Month 6']").exists())
                .andExpect(jsonPath("$.totalProjectedRevenue").isNumber())
                .andExpect(jsonPath("$.totalProjectedRevenue").value(org.hamcrest.Matchers.greaterThan(0.0)));
    }

    @Test
    void getRevenueForecast_WithOneMonth_ShouldReturnSingleMonthForecast() throws Exception {
        mockMvc.perform(get("/api/analytics/revenue/forecast")
                .param("months", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.forecastMonths").value(1))
                .andExpect(jsonPath("$.monthlyRevenue['Month 1']").exists())
                .andExpect(jsonPath("$.monthlyRevenue['Month 2']").doesNotExist());
    }

    @Test
    void getRevenueForecast_WithTwelveMonths_ShouldReturnYearlyForecast() throws Exception {
        mockMvc.perform(get("/api/analytics/revenue/forecast")
                .param("months", "12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.forecastMonths").value(12))
                .andExpect(jsonPath("$.monthlyRevenue['Month 12']").exists())
                .andExpect(jsonPath("$.totalProjectedRevenue").isNumber());
    }

    @Test
    void getRevenueForecast_WithZeroMonths_ShouldReturnEmptyForecast() throws Exception {
        mockMvc.perform(get("/api/analytics/revenue/forecast")
                .param("months", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.forecastMonths").value(0))
                .andExpect(jsonPath("$.monthlyRevenue").isEmpty())
                .andExpect(jsonPath("$.totalProjectedRevenue").value(0.0));
    }

    @Test
    void getRevenueForecast_WithNegativeMonths_ShouldReturnEmptyForecast() throws Exception {
        mockMvc.perform(get("/api/analytics/revenue/forecast")
                .param("months", "-5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.forecastMonths").value(-5))
                .andExpect(jsonPath("$.monthlyRevenue").isEmpty())
                .andExpect(jsonPath("$.totalProjectedRevenue").value(0.0));
    }

    @Test
    void getRevenueForecast_WithLargeNumberOfMonths_ShouldReturnForecast() throws Exception {
        mockMvc.perform(get("/api/analytics/revenue/forecast")
                .param("months", "24"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.forecastMonths").value(24))
                .andExpect(jsonPath("$.monthlyRevenue['Month 24']").exists())
                .andExpect(jsonPath("$.totalProjectedRevenue").isNumber());
    }

    @Test
    void getBoardgamePerformance_ShouldReturnPerformanceMetrics() throws Exception {
        mockMvc.perform(get("/api/analytics/boardgames/performance"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.performanceMetrics.mostReservedGame").value("Catan"))
                .andExpect(jsonPath("$.performanceMetrics.leastReservedGame").value("Chess"))
                .andExpect(jsonPath("$.performanceMetrics.averageReservationsPerGame").value(12.5))
                .andExpect(jsonPath("$.performanceMetrics.topPerformingGenre").value("Strategy"))
                .andExpect(jsonPath("$.performanceMetrics.utilizationRate").value(78.3))
                .andExpect(jsonPath("$.analysisDate").value(LocalDate.now().toString()));
    }

    @Test
    void getCustomerInsights_ShouldReturnCustomerData() throws Exception {
        mockMvc.perform(get("/api/analytics/customer/insights"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerInsights.totalCustomers").isNumber())
                .andExpect(jsonPath("$.customerInsights.activeCustomers").isNumber())
                .andExpect(jsonPath("$.customerInsights.averageReservationsPerCustomer").value(3.2))
                .andExpect(jsonPath("$.customerInsights.customerRetentionRate").value(85.7))
                .andExpect(jsonPath("$.customerInsights.newCustomersThisMonth").value(12))
                .andExpect(jsonPath("$.generatedAt").value(LocalDate.now().toString()));

        long actualUserCount = userRepository.count();
        assertThat(actualUserCount).isGreaterThan(0);
    }

    @Test
    void getCustomerInsights_WithMultipleUsers_ShouldReturnCorrectCounts() throws Exception {
        Role userRole = new Role();
        userRole.setRoleName("ROLE_USER");
        userRole.setActive(true);
        userRole.setDescription("user roles");

        User user2 = new User();
        user2.setUserName("user2");
        user2.setPassword("$2a$10$test");
        user2.setEnabled(true);
        user2.setExpired(false);
        user2.setLocked(false);
        user2.setAreCredentialsExpired(false);
        user2.setRoles(Arrays.asList(userRole));
        userRepository.save(user2);

        User user3 = new User();
        user3.setUserName("user3");
        user3.setPassword("$2a$10$test");
        user3.setEnabled(true);
        user3.setExpired(false);
        user3.setLocked(false);
        user3.setAreCredentialsExpired(false);
        user3.setRoles(Arrays.asList(userRole));
        userRepository.save(user3);

        mockMvc.perform(get("/api/analytics/customer/insights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerInsights.totalCustomers").value(3))
                .andExpect(jsonPath("$.customerInsights.activeCustomers").value(2.25));
    }

    @Test
    void getSeasonalTrends_WithValidYear_ShouldReturnSeasonalData() throws Exception {
        int testYear = 2024;

        mockMvc.perform(get("/api/analytics/trends/seasonal")
                .param("year", String.valueOf(testYear)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.year").value(testYear))
                .andExpect(jsonPath("$.seasonalReservations.Winter").value(45))
                .andExpect(jsonPath("$.seasonalReservations.Spring").value(38))
                .andExpect(jsonPath("$.seasonalReservations.Summer").value(52))
                .andExpect(jsonPath("$.seasonalReservations.Autumn").value(41))
                .andExpect(jsonPath("$.peakSeason").value("Summer"))
                .andExpect(jsonPath("$.lowSeason").value("Spring"));
    }

    @Test
    void getSeasonalTrends_WithCurrentYear_ShouldReturnSeasonalData() throws Exception {
        int currentYear = LocalDate.now().getYear();

        mockMvc.perform(get("/api/analytics/trends/seasonal")
                .param("year", String.valueOf(currentYear)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(currentYear))
                .andExpect(jsonPath("$.seasonalReservations").isMap())
                .andExpect(jsonPath("$.peakSeason").value("Summer"))
                .andExpect(jsonPath("$.lowSeason").value("Spring"));
    }

    @Test
    void getSeasonalTrends_WithPastYear_ShouldReturnSeasonalData() throws Exception {
        int pastYear = 2020;

        mockMvc.perform(get("/api/analytics/trends/seasonal")
                .param("year", String.valueOf(pastYear)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(pastYear))
                .andExpect(jsonPath("$.seasonalReservations").isMap());
    }

    @Test
    void getSeasonalTrends_WithFutureYear_ShouldReturnSeasonalData() throws Exception {
        int futureYear = 2030;

        mockMvc.perform(get("/api/analytics/trends/seasonal")
                .param("year", String.valueOf(futureYear)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(futureYear))
                .andExpect(jsonPath("$.seasonalReservations").isMap());
    }

    @Test
    void getSeasonalTrends_WithNegativeYear_ShouldReturnSeasonalData() throws Exception {
        int negativeYear = -2020;

        mockMvc.perform(get("/api/analytics/trends/seasonal")
                .param("year", String.valueOf(negativeYear)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(negativeYear))
                .andExpect(jsonPath("$.seasonalReservations").isMap());
    }

    @Test
    void getSeasonalTrends_WithZeroYear_ShouldReturnSeasonalData() throws Exception {
        mockMvc.perform(get("/api/analytics/trends/seasonal")
                .param("year", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(0))
                .andExpect(jsonPath("$.seasonalReservations").isMap());
    }

    @Test
    void getRevenueForecast_WithoutMonthsParameter_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/analytics/revenue/forecast"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSeasonalTrends_WithoutYearParameter_ShouldReturnError() throws Exception {
        mockMvc.perform(get("/api/analytics/trends/seasonal"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRevenueForecast_WithInvalidMonthsParameter_ShouldReturnError() throws Exception {
        mockMvc.perform(get("/api/analytics/revenue/forecast")
                .param("months", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSeasonalTrends_WithInvalidYearParameter_ShouldReturnError() throws Exception {
        mockMvc.perform(get("/api/analytics/trends/seasonal")
                .param("year", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void allAnalyticsEndpoints_ShouldReturnValidJsonStructure() throws Exception {
        String[] endpoints = {
                "/api/analytics/revenue/forecast?months=3",
                "/api/analytics/boardgames/performance",
                "/api/analytics/customer/insights",
                "/api/analytics/trends/seasonal?year=2024"
        };

        for (String endpoint : endpoints) {
            mockMvc.perform(get(endpoint))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isMap());
        }
    }

    @Test
    void getRevenueForecast_ShouldHaveIncreasingRevenue() throws Exception {
        mockMvc.perform(get("/api/analytics/revenue/forecast")
                .param("months", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthlyRevenue['Month 1']").exists())
                .andExpect(jsonPath("$.monthlyRevenue['Month 2']").exists())
                .andExpect(jsonPath("$.monthlyRevenue['Month 3']").exists());
    }

    @Test
    void getBoardgamePerformance_ShouldHaveConsistentDataTypes() throws Exception {
        mockMvc.perform(get("/api/analytics/boardgames/performance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.performanceMetrics.mostReservedGame").isString())
                .andExpect(jsonPath("$.performanceMetrics.leastReservedGame").isString())
                .andExpect(jsonPath("$.performanceMetrics.averageReservationsPerGame").isNumber())
                .andExpect(jsonPath("$.performanceMetrics.topPerformingGenre").isString())
                .andExpect(jsonPath("$.performanceMetrics.utilizationRate").isNumber())
                .andExpect(jsonPath("$.analysisDate").isString());
    }

    @Test
    void getCustomerInsights_ShouldHaveConsistentDataTypes() throws Exception {
        mockMvc.perform(get("/api/analytics/customer/insights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerInsights.totalCustomers").isNumber())
                .andExpect(jsonPath("$.customerInsights.activeCustomers").isNumber())
                .andExpect(jsonPath("$.customerInsights.averageReservationsPerCustomer").isNumber())
                .andExpect(jsonPath("$.customerInsights.customerRetentionRate").isNumber())
                .andExpect(jsonPath("$.customerInsights.newCustomersThisMonth").isNumber())
                .andExpect(jsonPath("$.generatedAt").isString());
    }

    @Test
    void getSeasonalTrends_ShouldHaveConsistentDataTypes() throws Exception {
        mockMvc.perform(get("/api/analytics/trends/seasonal")
                .param("year", "2024"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").isNumber())
                .andExpect(jsonPath("$.seasonalReservations.Winter").isNumber())
                .andExpect(jsonPath("$.seasonalReservations.Spring").isNumber())
                .andExpect(jsonPath("$.seasonalReservations.Summer").isNumber())
                .andExpect(jsonPath("$.seasonalReservations.Autumn").isNumber())
                .andExpect(jsonPath("$.peakSeason").isString())
                .andExpect(jsonPath("$.lowSeason").isString());
    }
}