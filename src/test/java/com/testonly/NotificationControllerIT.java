package com.testonly;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ContextConfiguration;
import com.meepleconnect.boardgamesapi.MeepleConnectBoardgamesApiApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(NotificationController.class)
@ContextConfiguration(classes = NotificationControllerIT.TestConfig.class)
class NotificationControllerIT {

    @Configuration
    @ComponentScan(basePackages = "com.testonly", includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = NotificationController.class))
    @EnableWebSecurity
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllNotifications_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].type", is("RESERVATION_REMINDER")))
                .andExpect(jsonPath("$[0].title", is("Reservering herinnering")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].type", is("SYSTEM_UPDATE")));
    }

    @Test
    void getNotificationById_ShouldReturnNotification() throws Exception {
        mockMvc.perform(get("/api/notifications/123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(123)))
                .andExpect(jsonPath("$.type", is("RESERVATION_CONFIRMATION")))
                .andExpect(jsonPath("$.title", is("Reservering bevestigd")));
    }

    @Test
    void createNotification_ShouldReturnCreatedNotification() throws Exception {
        Map<String, Object> notificationRequest = new HashMap<>();
        notificationRequest.put("type", "SYSTEM_ALERT");
        notificationRequest.put("title", "Test notification");
        notificationRequest.put("message", "This is a test notification");
        notificationRequest.put("priority", "HIGH");

        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type", is("SYSTEM_ALERT")))
                .andExpect(jsonPath("$.title", is("Test notification")))
                .andExpect(jsonPath("$.message", is("This is a test notification")))
                .andExpect(jsonPath("$.priority", is("HIGH")))
                .andExpect(jsonPath("$.read", is(false)));
    }

    @Test
    void markAsRead_ShouldReturnStatus() throws Exception {
        mockMvc.perform(put("/api/notifications/123/read"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(123)))
                .andExpect(jsonPath("$.status", is("MARKED_AS_READ")));
    }

    @Test
    void deleteNotification_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/notifications/123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUnreadCount_ShouldReturnCounts() throws Exception {
        mockMvc.perform(get("/api/notifications/unread/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.unreadCount", is(5)))
                .andExpect(jsonPath("$.totalCount", is(12)));
    }

    @Test
    void broadcastNotification_ShouldReturnBroadcastInfo() throws Exception {
        Map<String, Object> broadcastRequest = new HashMap<>();
        broadcastRequest.put("message", "System maintenance scheduled");
        broadcastRequest.put("priority", "HIGH");

        mockMvc.perform(post("/api/notifications/broadcast")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(broadcastRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("System maintenance scheduled")))
                .andExpect(jsonPath("$.priority", is("HIGH")))
                .andExpect(jsonPath("$.recipients", is(150)));
    }
}