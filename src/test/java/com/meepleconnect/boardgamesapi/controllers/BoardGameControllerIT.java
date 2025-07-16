package com.meepleconnect.boardgamesapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleconnect.boardgamesapi.dtos.BoardgameRequestDTO;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.models.Publisher;
import com.meepleconnect.boardgamesapi.repositories.BoardgameRepository;
import com.meepleconnect.boardgamesapi.repositories.PublisherRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
public class BoardgameControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BoardgameRepository boardgameRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private Publisher testPublisher;
    private Boardgame testBoardgame;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        testPublisher = new Publisher();
        testPublisher.setName("Test Publisher");
        testPublisher.setCountryOfOrigin("Netherlands");
        testPublisher.setFounded(2020);
        testPublisher.setIndie(true);
        testPublisher = publisherRepository.save(testPublisher);

        testBoardgame = new Boardgame();
        testBoardgame.setName("Test Game");
        testBoardgame.setPrice(new BigDecimal("29.99"));
        testBoardgame.setAvailable(true);
        testBoardgame.setMinPlayers(2);
        testBoardgame.setMaxPlayers(4);
        testBoardgame.setGenre("Strategy");
        testBoardgame.setPublisher(testPublisher);
        testBoardgame = boardgameRepository.save(testBoardgame);
    }

    @Test
    void getAllBoardgames_ShouldReturnAllBoardgames() throws Exception {
        Boardgame secondGame = new Boardgame();
        secondGame.setName("Second Game");
        secondGame.setPrice(new BigDecimal("39.99"));
        secondGame.setAvailable(false);
        secondGame.setMinPlayers(1);
        secondGame.setMaxPlayers(6);
        secondGame.setGenre("Family");
        secondGame.setPublisher(testPublisher);
        boardgameRepository.save(secondGame);

        mockMvc.perform(get("/api/boardgames"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(testBoardgame.getId()))
                .andExpect(jsonPath("$[0].name").value("Test Game"))
                .andExpect(jsonPath("$[0].price").value(29.99))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[0].minPlayers").value(2))
                .andExpect(jsonPath("$[0].maxPlayers").value(4))
                .andExpect(jsonPath("$[0].genre").value("Strategy"))
                .andExpect(jsonPath("$[0].publisher.id").value(testPublisher.getId()))
                .andExpect(jsonPath("$[0].publisher.name").value("Test Publisher"))
                .andExpect(jsonPath("$[1].name").value("Second Game"))
                .andExpect(jsonPath("$[1].available").value(false));
    }

    @Test
    void getAllBoardgames_WithGenreFilter_ShouldReturnFilteredBoardgames() throws Exception {
        Boardgame familyGame = new Boardgame();
        familyGame.setName("Family Game");
        familyGame.setPrice(new BigDecimal("19.99"));
        familyGame.setAvailable(true);
        familyGame.setMinPlayers(2);
        familyGame.setMaxPlayers(8);
        familyGame.setGenre("Family");
        familyGame.setPublisher(testPublisher);
        boardgameRepository.save(familyGame);

        mockMvc.perform(get("/api/boardgames")
                        .param("genre", "Strategy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].genre").value("Strategy"));
    }

    @Test
    void getAllBoardgames_WithAvailableFilter_ShouldReturnFilteredBoardgames() throws Exception {
        Boardgame unavailableGame = new Boardgame();
        unavailableGame.setName("Unavailable Game");
        unavailableGame.setPrice(new BigDecimal("49.99"));
        unavailableGame.setAvailable(false);
        unavailableGame.setMinPlayers(3);
        unavailableGame.setMaxPlayers(5);
        unavailableGame.setGenre("Adventure");
        unavailableGame.setPublisher(testPublisher);
        boardgameRepository.save(unavailableGame);

        mockMvc.perform(get("/api/boardgames")
                        .param("available", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    void getAllBoardgames_WithMinPlayersFilter_ShouldReturnFilteredBoardgames() throws Exception {
        Boardgame highPlayerGame = new Boardgame();
        highPlayerGame.setName("High Player Game");
        highPlayerGame.setPrice(new BigDecimal("59.99"));
        highPlayerGame.setAvailable(true);
        highPlayerGame.setMinPlayers(4);
        highPlayerGame.setMaxPlayers(8);
        highPlayerGame.setGenre("Party");
        highPlayerGame.setPublisher(testPublisher);
        boardgameRepository.save(highPlayerGame);

        mockMvc.perform(get("/api/boardgames")
                        .param("minPlayers", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].minPlayers").value(4));
    }

    @Test
    void getAllBoardgames_WithMaxPlayersFilter_ShouldReturnFilteredBoardgames() throws Exception {
        Boardgame lowPlayerGame = new Boardgame();
        lowPlayerGame.setName("Low Player Game");
        lowPlayerGame.setPrice(new BigDecimal("15.99"));
        lowPlayerGame.setAvailable(true);
        lowPlayerGame.setMinPlayers(1);
        lowPlayerGame.setMaxPlayers(2);
        lowPlayerGame.setGenre("Abstract");
        lowPlayerGame.setPublisher(testPublisher);
        boardgameRepository.save(lowPlayerGame);

        mockMvc.perform(get("/api/boardgames")
                        .param("maxPlayers", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].maxPlayers").value(2));
    }

    @Test
    void getAllBoardgames_WithMultipleFilters_ShouldReturnFilteredBoardgames() throws Exception {
        Boardgame filteredGame = new Boardgame();
        filteredGame.setName("Filtered Game");
        filteredGame.setPrice(new BigDecimal("25.99"));
        filteredGame.setAvailable(true);
        filteredGame.setMinPlayers(3);
        filteredGame.setMaxPlayers(5);
        filteredGame.setGenre("Strategy");
        filteredGame.setPublisher(testPublisher);
        boardgameRepository.save(filteredGame);

        mockMvc.perform(get("/api/boardgames")
                        .param("genre", "Strategy")
                        .param("available", "true")
                        .param("minPlayers", "2")
                        .param("maxPlayers", "6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].genre").value("Strategy"))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    void getBoardgameById_WithValidId_ShouldReturnBoardgame() throws Exception {
        mockMvc.perform(get("/api/boardgames/{id}", testBoardgame.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testBoardgame.getId()))
                .andExpect(jsonPath("$.name").value("Test Game"))
                .andExpect(jsonPath("$.price").value(29.99))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.minPlayers").value(2))
                .andExpect(jsonPath("$.maxPlayers").value(4))
                .andExpect(jsonPath("$.genre").value("Strategy"))
                .andExpect(jsonPath("$.publisher.id").value(testPublisher.getId()))
                .andExpect(jsonPath("$.publisher.name").value("Test Publisher"));
    }

    @Test
    void getBoardgameById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/boardgames/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Game Not Found"))
                .andExpect(jsonPath("$.message").value("Boardgame with ID 999 not found."));
    }

    @Test
    void addBoardgame_WithValidData_ShouldCreateBoardgame() throws Exception {
        BoardgameRequestDTO requestDTO = new BoardgameRequestDTO();
        requestDTO.setName("New Game");
        requestDTO.setPrice(new BigDecimal("45.99"));
        requestDTO.setAvailable(true);
        requestDTO.setMinPlayers(2);
        requestDTO.setMaxPlayers(6);
        requestDTO.setGenre("Adventure");
        requestDTO.setPublisherId(testPublisher.getId());

        mockMvc.perform(post("/api/boardgames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/boardgames/" + (testBoardgame.getId() + 1)))
                .andExpect(jsonPath("$.name").value("New Game"))
                .andExpect(jsonPath("$.price").value(45.99))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.minPlayers").value(2))
                .andExpect(jsonPath("$.maxPlayers").value(6))
                .andExpect(jsonPath("$.genre").value("Adventure"))
                .andExpect(jsonPath("$.publisher.id").value(testPublisher.getId()));

        List<Boardgame> boardgames = boardgameRepository.findAll();
        assertThat(boardgames).hasSize(2);
        assertThat(boardgames.stream().anyMatch(bg -> bg.getName().equals("New Game"))).isTrue();
    }

    @Test
    void addBoardgame_WithEmptyName_ShouldReturnBadRequest() throws Exception {
        BoardgameRequestDTO requestDTO = new BoardgameRequestDTO();
        requestDTO.setName("");
        requestDTO.setPrice(new BigDecimal("45.99"));
        requestDTO.setAvailable(true);
        requestDTO.setMinPlayers(2);
        requestDTO.setMaxPlayers(6);
        requestDTO.setGenre("Adventure");
        requestDTO.setPublisherId(testPublisher.getId());

        mockMvc.perform(post("/api/boardgames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBoardgame_WithNullName_ShouldReturnBadRequest() throws Exception {
        BoardgameRequestDTO requestDTO = new BoardgameRequestDTO();
        requestDTO.setName(null);
        requestDTO.setPrice(new BigDecimal("45.99"));
        requestDTO.setAvailable(true);
        requestDTO.setMinPlayers(2);
        requestDTO.setMaxPlayers(6);
        requestDTO.setGenre("Adventure");
        requestDTO.setPublisherId(testPublisher.getId());

        mockMvc.perform(post("/api/boardgames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBoardgame_WithInvalidPrice_ShouldReturnBadRequest() throws Exception {
        BoardgameRequestDTO requestDTO = new BoardgameRequestDTO();
        requestDTO.setName("New Game");
        requestDTO.setPrice(new BigDecimal("-10.00"));
        requestDTO.setAvailable(true);
        requestDTO.setMinPlayers(2);
        requestDTO.setMaxPlayers(6);
        requestDTO.setGenre("Adventure");
        requestDTO.setPublisherId(testPublisher.getId());

        mockMvc.perform(post("/api/boardgames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBoardgame_WithInvalidMinPlayers_ShouldReturnBadRequest() throws Exception {
        BoardgameRequestDTO requestDTO = new BoardgameRequestDTO();
        requestDTO.setName("New Game");
        requestDTO.setPrice(new BigDecimal("45.99"));
        requestDTO.setAvailable(true);
        requestDTO.setMinPlayers(0);
        requestDTO.setMaxPlayers(6);
        requestDTO.setGenre("Adventure");
        requestDTO.setPublisherId(testPublisher.getId());

        mockMvc.perform(post("/api/boardgames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBoardgame_WithInvalidMaxPlayers_ShouldReturnBadRequest() throws Exception {
        BoardgameRequestDTO requestDTO = new BoardgameRequestDTO();
        requestDTO.setName("New Game");
        requestDTO.setPrice(new BigDecimal("45.99"));
        requestDTO.setAvailable(true);
        requestDTO.setMinPlayers(2);
        requestDTO.setMaxPlayers(25);
        requestDTO.setGenre("Adventure");
        requestDTO.setPublisherId(testPublisher.getId());

        mockMvc.perform(post("/api/boardgames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBoardgame_WithInvalidGenre_ShouldReturnBadRequest() throws Exception {
        BoardgameRequestDTO requestDTO = new BoardgameRequestDTO();
        requestDTO.setName("New Game");
        requestDTO.setPrice(new BigDecimal("45.99"));
        requestDTO.setAvailable(true);
        requestDTO.setMinPlayers(2);
        requestDTO.setMaxPlayers(6);
        requestDTO.setGenre("");
        requestDTO.setPublisherId(testPublisher.getId());

        mockMvc.perform(post("/api/boardgames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBoardgame_WithInvalidPublisherId_ShouldReturnBadRequest() throws Exception {
        BoardgameRequestDTO requestDTO = new BoardgameRequestDTO();
        requestDTO.setName("New Game");
        requestDTO.setPrice(new BigDecimal("45.99"));
        requestDTO.setAvailable(true);
        requestDTO.setMinPlayers(2);
        requestDTO.setMaxPlayers(6);
        requestDTO.setGenre("Adventure");
        requestDTO.setPublisherId(999L);

        mockMvc.perform(post("/api/boardgames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBoardgame_WithDuplicateName_ShouldReturnConflict() throws Exception {
        BoardgameRequestDTO requestDTO = new BoardgameRequestDTO();
        requestDTO.setName("Test Game");
        requestDTO.setPrice(new BigDecimal("45.99"));
        requestDTO.setAvailable(true);
        requestDTO.setMinPlayers(2);
        requestDTO.setMaxPlayers(6);
        requestDTO.setGenre("Adventure");
        requestDTO.setPublisherId(testPublisher.getId());

        mockMvc.perform(post("/api/boardgames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Boardagme with name 'Test Game' already exists."));
    }

    @Test
    void updateBoardgame_WithValidData_ShouldUpdateBoardgame() throws Exception {
        BoardgameRequestDTO requestDTO = new BoardgameRequestDTO();
        requestDTO.setName("Updated Game");
        requestDTO.setPrice(new BigDecimal("59.99"));
        requestDTO.setAvailable(false);
        requestDTO.setMinPlayers(3);
        requestDTO.setMaxPlayers(8);
        requestDTO.setGenre("Party");
        requestDTO.setPublisherId(testPublisher.getId());

        mockMvc.perform(put("/api/boardgames/{id}", testBoardgame.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testBoardgame.getId()))
                .andExpect(jsonPath("$.name").value("Updated Game"))
                .andExpect(jsonPath("$.price").value(59.99))
                .andExpect(jsonPath("$.available").value(false))
                .andExpect(jsonPath("$.minPlayers").value(3))
                .andExpect(jsonPath("$.maxPlayers").value(8))
                .andExpect(jsonPath("$.genre").value("Party"));

        Boardgame updatedBoardgame = boardgameRepository.findById(testBoardgame.getId()).orElse(null);
        assertThat(updatedBoardgame).isNotNull();
        assertThat(updatedBoardgame.getName()).isEqualTo("Updated Game");
        assertThat(updatedBoardgame.getPrice()).isEqualTo(new BigDecimal("59.99"));
        assertThat(updatedBoardgame.isAvailable()).isFalse();
    }

    @Test
    void updateBoardgame_WithInvalidId_ShouldReturnNotFound() throws Exception {
        BoardgameRequestDTO requestDTO = new BoardgameRequestDTO();
        requestDTO.setName("Updated Game");
        requestDTO.setPrice(new BigDecimal("59.99"));
        requestDTO.setAvailable(false);
        requestDTO.setMinPlayers(3);
        requestDTO.setMaxPlayers(8);
        requestDTO.setGenre("Party");
        requestDTO.setPublisherId(testPublisher.getId());

        mockMvc.perform(put("/api/boardgames/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Game Not Found"))
                .andExpect(jsonPath("$.message").value("Boardgame with ID 999 not found."));
    }

    @Test
    void updateBoardgame_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        BoardgameRequestDTO requestDTO = new BoardgameRequestDTO();
        requestDTO.setName("");
        requestDTO.setPrice(new BigDecimal("59.99"));
        requestDTO.setAvailable(false);
        requestDTO.setMinPlayers(3);
        requestDTO.setMaxPlayers(8);
        requestDTO.setGenre("Party");
        requestDTO.setPublisherId(testPublisher.getId());

        mockMvc.perform(put("/api/boardgames/{id}", testBoardgame.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteBoardgame_WithValidId_ShouldDeleteBoardgame() throws Exception {
        mockMvc.perform(delete("/api/boardgames/{id}", testBoardgame.getId()))
                .andExpect(status().isNoContent());

        assertThat(boardgameRepository.findById(testBoardgame.getId())).isEmpty();
    }

    @Test
    void deleteBoardgame_WithInvalidId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/boardgames/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Game Not Found"))
                .andExpect(jsonPath("$.message").value("Boardgame with ID 999 not found."));
    }

    @Test
    void getAllBoardgames_WithNoFilters_ShouldReturnAllBoardgames() throws Exception {
        mockMvc.perform(get("/api/boardgames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Test Game"));
    }

    @Test
    void getAllBoardgames_WithNonMatchingFilters_ShouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/boardgames")
                        .param("genre", "NonExistentGenre"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(0)));
    }
} 