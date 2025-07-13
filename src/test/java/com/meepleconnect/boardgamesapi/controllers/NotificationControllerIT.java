package com.meepleconnect.boardgamesapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
public class NotificationControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllNotifications_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].type", is("RESERVATION_REMINDER")))
                .andExpect(jsonPath("$[0].title", is("Reservering herinnering")))
                .andExpect(jsonPath("$[0].message", is("Je hebt morgen een reservering voor Catan")))
                .andExpect(jsonPath("$[0].read", is(false)))
                .andExpect(jsonPath("$[0].priority", is("MEDIUM")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].type", is("SYSTEM_UPDATE")))
                .andExpect(jsonPath("$[1].title", is("Systeem update")))
                .andExpect(jsonPath("$[1].message", is("Nieuwe bordspellen toegevoegd aan de collectie")))
                .andExpect(jsonPath("$[1].read", is(true)))
                .andExpect(jsonPath("$[1].priority", is("LOW")));
    }

    @Test
    void getAllNotifications_ShouldReturnValidNotificationStructure() throws Exception {
        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").exists())
                .andExpect(jsonPath("$[*].type").exists())
                .andExpect(jsonPath("$[*].title").exists())
                .andExpect(jsonPath("$[*].message").exists())
                .andExpect(jsonPath("$[*].timestamp").exists())
                .andExpect(jsonPath("$[*].read").exists())
                .andExpect(jsonPath("$[*].priority").exists());
    }

    @Test
    void getNotificationById_ShouldReturnNotification() throws Exception {
        mockMvc.perform(get("/api/notifications/123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(123)))
                .andExpect(jsonPath("$.type", is("RESERVATION_CONFIRMATION")))
                .andExpect(jsonPath("$.title", is("Reservering bevestigd")))
                .andExpect(jsonPath("$.message", is("Je reservering voor Ticket to Ride is bevestigd")))
                .andExpect(jsonPath("$.read", is(false)))
                .andExpect(jsonPath("$.priority", is("HIGH")));
    }

    @Test
    void getNotificationById_WithNonNumericId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/notifications/invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNotification_WithValidData_ShouldReturnCreatedNotification() throws Exception {
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
                .andExpect(jsonPath("$.read", is(false)))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void createNotification_WithMinimalData_ShouldReturnCreatedNotification() throws Exception {
        Map<String, Object> notificationRequest = new HashMap<>();
        notificationRequest.put("type", "INFO");
        notificationRequest.put("title", "Minimal notification");
        notificationRequest.put("message", "Minimal test");

        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type", is("INFO")))
                .andExpect(jsonPath("$.title", is("Minimal notification")))
                .andExpect(jsonPath("$.message", is("Minimal test")))
                .andExpect(jsonPath("$.priority", is("MEDIUM")))
                .andExpect(jsonPath("$.read", is(false)));
    }

    @Test
    void createNotification_WithInvalidJson_ShouldReturnServerError() throws Exception {
        String invalidJson = "{ invalid json }";

        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void markAsRead_ShouldReturnStatus() throws Exception {
        mockMvc.perform(put("/api/notifications/123/read"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(123)))
                .andExpect(jsonPath("$.status", is("MARKED_AS_READ")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void markAsRead_WithNonNumericId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/api/notifications/invalid/read"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteNotification_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/notifications/123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNotification_WithNonNumericId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/notifications/invalid"))
                .andExpect(status().isBadRequest());
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
    void broadcastNotification_WithValidData_ShouldReturnBroadcastInfo() throws Exception {
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
                .andExpect(jsonPath("$.recipients", is(150)))
                .andExpect(jsonPath("$.broadcastId").exists())
                .andExpect(jsonPath("$.sentAt").exists());
    }

    @Test
    void broadcastNotification_WithMinimalData_ShouldReturnBroadcastInfo() throws Exception {
        Map<String, Object> broadcastRequest = new HashMap<>();
        broadcastRequest.put("message", "Simple broadcast");

        mockMvc.perform(post("/api/notifications/broadcast")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(broadcastRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("Simple broadcast")))
                .andExpect(jsonPath("$.recipients", is(150)))
                .andExpect(jsonPath("$.broadcastId").exists())
                .andExpect(jsonPath("$.sentAt").exists());
    }

    @Test
    void broadcastNotification_WithInvalidJson_ShouldReturnServerError() throws Exception {
        String invalidJson = "{ invalid json }";

        mockMvc.perform(post("/api/notifications/broadcast")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createNotification_WithDifferentTypes_ShouldReturnCreatedNotification() throws Exception {
        String[] notificationTypes = {"RESERVATION_REMINDER", "SYSTEM_UPDATE", "RESERVATION_CONFIRMATION", "SYSTEM_ALERT", "INFO"};
        
        for (String type : notificationTypes) {
            Map<String, Object> notificationRequest = new HashMap<>();
            notificationRequest.put("type", type);
            notificationRequest.put("title", "Test " + type);
            notificationRequest.put("message", "Test message for " + type);
            notificationRequest.put("priority", "MEDIUM");

            mockMvc.perform(post("/api/notifications")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(notificationRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.type", is(type)))
                    .andExpect(jsonPath("$.title", is("Test " + type)))
                    .andExpect(jsonPath("$.message", is("Test message for " + type)));
        }
    }

    @Test
    void createNotification_WithDifferentPriorities_ShouldReturnCreatedNotification() throws Exception {
        String[] priorities = {"LOW", "MEDIUM", "HIGH"};
        
        for (String priority : priorities) {
            Map<String, Object> notificationRequest = new HashMap<>();
            notificationRequest.put("type", "TEST");
            notificationRequest.put("title", "Priority test");
            notificationRequest.put("message", "Testing priority " + priority);
            notificationRequest.put("priority", priority);

            mockMvc.perform(post("/api/notifications")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(notificationRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.priority", is(priority)));
        }
    }
} 