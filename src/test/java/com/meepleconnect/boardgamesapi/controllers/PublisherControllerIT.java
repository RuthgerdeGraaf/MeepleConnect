package com.meepleconnect.boardgamesapi.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleconnect.boardgamesapi.models.Publisher;
import com.meepleconnect.boardgamesapi.repositories.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PublisherControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Publisher testPublisher;

    @BeforeEach
    void setUp() {
        publisherRepository.deleteAll();
        testPublisher = new Publisher();
        testPublisher.setName("Test Publisher");
        testPublisher.setCountryOfOrigin("Netherlands");
        testPublisher.setFounded(2000);
        testPublisher.setIndie(true);
        publisherRepository.save(testPublisher);
    }

    @Test
    @WithMockUser
    void getAllPublishers_ShouldReturnList() throws Exception {
        String jsonResponse = mockMvc.perform(get("/api/publishers"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Publisher> publishers = objectMapper.readValue(jsonResponse, new TypeReference<List<Publisher>>() {});

        assertFalse(publishers.isEmpty());
        assertEquals("Test Publisher", publishers.get(0).getName());
    }

    @Test
    @WithMockUser
    void getPublisherById_ShouldReturnPublisher() throws Exception {
        mockMvc.perform(get("/api/publishers/" + testPublisher.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Publisher"));
    }

    @Test
    @WithMockUser
    void getPublisherById_NonExisting_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/publishers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void addPublisher_ShouldCreatePublisher() throws Exception {
        Publisher newPublisher = new Publisher();
        newPublisher.setName("New Publisher");
        newPublisher.setCountryOfOrigin("USA");
        newPublisher.setFounded(2010);
        newPublisher.setIndie(false);

        mockMvc.perform(post("/api/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPublisher)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Publisher"));
    }

    @Test
    void addPublisher_Unauthorized_ShouldReturn403() throws Exception {
        Publisher newPublisher = new Publisher();
        newPublisher.setName("New Publisher");
        newPublisher.setCountryOfOrigin("USA");
        newPublisher.setFounded(2010);
        newPublisher.setIndie(false);

        mockMvc.perform(post("/api/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPublisher)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void updatePublisher_ShouldUpdatePublisher() throws Exception {
        Publisher updatedPublisher = new Publisher();
        updatedPublisher.setName("Updated Publisher");
        updatedPublisher.setCountryOfOrigin("Germany");
        updatedPublisher.setFounded(2005);
        updatedPublisher.setIndie(false);

        mockMvc.perform(put("/api/publishers/" + testPublisher.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPublisher)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Publisher"));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void updatePublisher_NonExisting_ShouldReturn404() throws Exception {
        Publisher updatedPublisher = new Publisher();
        updatedPublisher.setName("Updated Publisher");
        updatedPublisher.setCountryOfOrigin("Germany");
        updatedPublisher.setFounded(2005);
        updatedPublisher.setIndie(false);

        mockMvc.perform(put("/api/publishers/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPublisher)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void deletePublisher_ShouldDeletePublisher() throws Exception {
        mockMvc.perform(delete("/api/publishers/" + testPublisher.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void deletePublisher_NonExisting_ShouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/publishers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePublisher_Unauthorized_ShouldReturn403() throws Exception {
        mockMvc.perform(delete("/api/publishers/" + testPublisher.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void getPublishersByCountry_ShouldReturnFilteredResults() throws Exception {
        Publisher dutchPublisher = new Publisher();
        dutchPublisher.setName("Dutch Publisher");
        dutchPublisher.setCountryOfOrigin("Netherlands");
        dutchPublisher.setFounded(2010);
        dutchPublisher.setIndie(true);

        Publisher usPublisher = new Publisher();
        usPublisher.setName("US Publisher");
        usPublisher.setCountryOfOrigin("USA");
        usPublisher.setFounded(2010);
        usPublisher.setIndie(false);

        publisherRepository.save(dutchPublisher);
        publisherRepository.save(usPublisher);

        String jsonResponse = mockMvc.perform(get("/api/publishers/country/Netherlands"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Publisher> filteredPublishers = objectMapper.readValue(jsonResponse, new TypeReference<List<Publisher>>() {});

        assertFalse(filteredPublishers.isEmpty());
        assertEquals(2, filteredPublishers.size());
        assertEquals("Test Publisher", filteredPublishers.get(0).getName());
        assertEquals("Dutch Publisher", filteredPublishers.get(1).getName());
    }
} 