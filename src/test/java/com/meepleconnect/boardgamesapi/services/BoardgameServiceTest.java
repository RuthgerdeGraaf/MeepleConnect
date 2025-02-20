package com.meepleconnect.boardgamesapi.services;

import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.repositories.BoardgameRepository;
import com.meepleconnect.boardgamesapi.exceptions.GameNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class BoardgameServiceTest {

    @Mock
    private BoardgameRepository boardgameRepository;

    @InjectMocks
    private BoardgameService boardgameService;

    private Boardgame testGame;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testGame = new Boardgame("Catan", new BigDecimal("39.99"), true, 2, 4, "Strategy", null);
        testGame.setId(1L); // Verwijder reflectie en gebruik setter
    }

    @Test
    void getAllBoardgames_ShouldReturnAll() {
        Boardgame game1 = new Boardgame("Terraforming Mars", new BigDecimal("59.99"), true, 1, 5, "Strategy", null);
        Boardgame game2 = new Boardgame("Gloomhaven", new BigDecimal("89.99"), true, 1, 4, "Adventure", null);

        when(boardgameRepository.findAll()).thenReturn(Arrays.asList(game1, game2));

        List<Boardgame> result = boardgameService.getAllBoardgames();

        assertEquals(2, result.size());
        verify(boardgameRepository, times(1)).findAll();
    }

    @Test
    void getBoardgameById_ShouldReturnBoardgame() {
        when(boardgameRepository.findById(1L)).thenReturn(Optional.of(testGame));

        Boardgame result = boardgameService.getBoardgameById(1L);

        assertNotNull(result);
        assertEquals("Catan", result.getName());
        verify(boardgameRepository, times(1)).findById(1L);
    }

    @Test
    void getBoardgameById_NotFound_ShouldThrowException() {
        when(boardgameRepository.findById(999L)).thenReturn(Optional.empty());

        GameNotFoundException exception = assertThrows(GameNotFoundException.class, () -> {
            boardgameService.getBoardgameById(999L);
        });

        assertEquals("Bordspel met ID 999 niet gevonden.", exception.getMessage());
        verify(boardgameRepository, times(1)).findById(999L);
    }

    @Test
    void addBoardgame_ShouldSaveBoardgame() {
        Boardgame newGame = new Boardgame("Terraforming Mars", new BigDecimal("59.99"), true, 1, 5, "Strategy", null);

        when(boardgameRepository.save(any(Boardgame.class))).thenReturn(newGame);

        Boardgame result = boardgameService.addBoardgame(newGame);

        assertNotNull(result);
        assertEquals("Terraforming Mars", result.getName());
        verify(boardgameRepository, times(1)).save(newGame);
    }

    @Test
    void updateBoardgame_ShouldUpdateExisting() {
        Boardgame existingGame = new Boardgame("Catan", new BigDecimal("39.99"), true, 2, 4, "Strategy", null);
        existingGame.setId(1L);
    
        Boardgame updatedGame = new Boardgame("Updated Catan", new BigDecimal("49.99"), true, 2, 5, "Adventure", null);
        updatedGame.setId(1L);
    
        when(boardgameRepository.findById(1L)).thenReturn(Optional.of(existingGame));
        when(boardgameRepository.save(any(Boardgame.class))).thenReturn(updatedGame);
    
        Boardgame result = boardgameService.updateBoardgame(1L, updatedGame);
    
        assertNotNull(result);
        assertEquals("Updated Catan", result.getName());
        verify(boardgameRepository, times(1)).save(any(Boardgame.class));
    }
    

    @Test
    void updateBoardgame_NotFound_ShouldThrowException() {
        Boardgame updatedGame = new Boardgame("Updated Catan", new BigDecimal("49.99"), true, 2, 5, "Adventure", null);

        when(boardgameRepository.findById(999L)).thenReturn(Optional.empty());

        GameNotFoundException exception = assertThrows(GameNotFoundException.class, () -> {
            boardgameService.updateBoardgame(999L, updatedGame);
        });

        assertEquals("Bordspel met ID 999 niet gevonden.", exception.getMessage());
        verify(boardgameRepository, times(1)).findById(999L);
    }

    @Test
    void deleteBoardgame_NotFound_ShouldThrowException() {
        when(boardgameRepository.existsById(999L)).thenReturn(false); // Gebruik existsById
    
        GameNotFoundException exception = assertThrows(GameNotFoundException.class, () -> {
            boardgameService.deleteBoardgame(999L);
        });
    
        assertEquals("Bordspel met ID 999 niet gevonden.", exception.getMessage());
        verify(boardgameRepository, times(1)).existsById(999L); // Verifieer existsById
    }
}
