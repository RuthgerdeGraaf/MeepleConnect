package com.meepleconnect.boardgamesapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleconnect.boardgamesapi.models.Publisher;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class PublisherControllerIT {

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
    void getAllPublishers_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/publishers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void getPublisherById_ShouldReturnPublisher() throws Exception {
        mockMvc.perform(get("/api/publishers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    void getPublishersByCountry_ShouldReturnPublishers() throws Exception {
        mockMvc.perform(get("/api/publishers/country/Netherlands"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void addPublisher_ShouldReturnCreatedPublisher() throws Exception {
        Publisher newPublisher = new Publisher();
        newPublisher.setName("Test Publisher");
        newPublisher.setCountryOfOrigin("Test Country");
        newPublisher.setFounded(2020);
        newPublisher.setIndie(true);

        mockMvc.perform(post("/api/publishers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPublisher)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Publisher"))
                .andExpect(jsonPath("$.countryOfOrigin").value("Test Country"))
                .andExpect(jsonPath("$.founded").value(2020))
                .andExpect(header().string("Location", containsString("/api/publishers/")));
    }

    @Test
    void updatePublisher_ShouldReturnUpdatedPublisher() throws Exception {
        // Eerst een publisher aanmaken met unieke naam
        Publisher newPublisher = new Publisher();
        newPublisher.setName("Publisher to Update " + System.currentTimeMillis());
        newPublisher.setCountryOfOrigin("Original Country");
        newPublisher.setFounded(2019);
        newPublisher.setIndie(false);

        String location = mockMvc.perform(post("/api/publishers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPublisher)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        String publisherId = location.substring(location.lastIndexOf("/") + 1);

        // Nu de publisher updaten
        Publisher updatedPublisher = new Publisher();
        updatedPublisher.setName("Updated Publisher");
        updatedPublisher.setCountryOfOrigin("Updated Country");
        updatedPublisher.setFounded(2021);
        updatedPublisher.setIndie(true);

        mockMvc.perform(put("/api/publishers/" + publisherId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPublisher)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Updated Publisher"))
                .andExpect(jsonPath("$.countryOfOrigin").value("Updated Country"))
                .andExpect(jsonPath("$.founded").value(2021));
    }

    @Test
    void deletePublisher_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/publishers/1"))
                .andExpect(status().isNoContent());
    }
}