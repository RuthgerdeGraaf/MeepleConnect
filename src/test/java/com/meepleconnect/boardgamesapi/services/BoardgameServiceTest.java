package com.meepleconnect.boardgamesapi.services;

import com.meepleconnect.boardgamesapi.exceptions.BadRequestException;
import com.meepleconnect.boardgamesapi.exceptions.ConflictException;
import com.meepleconnect.boardgamesapi.exceptions.GameNotFoundException;
import com.meepleconnect.boardgamesapi.exceptions.TeapotException;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.repositories.BoardgameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BoardgameServiceTest {

    @Mock
    private BoardgameRepository boardgameRepository;

    @InjectMocks
    private BoardgameService boardgameService;

    private Boardgame testGame;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testGame = new Boardgame("Catan", new BigDecimal("39.99"), true, 3, 4, "Strategy", null);
    }

    @Test
    void getAllBoardgames_ShouldReturnList() {
        when(boardgameRepository.findAll()).thenReturn(List.of(testGame));

        List<Boardgame> result = boardgameService.getAllBoardgames();

        assertEquals(1, result.size());
        assertEquals("Catan", result.get(0).getName());
        verify(boardgameRepository, times(1)).findAll();
    }

    @Test
    void getBoardgamesByGenre_ShouldReturnList() {
        when(boardgameRepository.findByGenreIgnoreCase("Strategy")).thenReturn(List.of(testGame));

        List<Boardgame> result = boardgameService.getBoardgamesByGenre("Strategy");

        assertEquals(1, result.size());
        assertEquals("Catan", result.get(0).getName());
        verify(boardgameRepository, times(1)).findByGenreIgnoreCase("Strategy");
    }

    @Test
    void getBoardgameById_ExistingId_ShouldReturnGame() {
        when(boardgameRepository.findById(1L)).thenReturn(Optional.of(testGame));

        Boardgame result = boardgameService.getBoardgameById(1L);

        assertNotNull(result);
        assertEquals("Catan", result.getName());
        verify(boardgameRepository, times(1)).findById(1L);
    }

    @Test
    void getBoardgameById_NonExistingId_ShouldThrowException() {
        when(boardgameRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> boardgameService.getBoardgameById(99L));
        verify(boardgameRepository, times(1)).findById(99L);
    }

    @Test
    void addBoardgame_NewGame_ShouldSaveAndReturn() {
        when(boardgameRepository.findByNameIgnoreCase("Catan")).thenReturn(Optional.empty());
        when(boardgameRepository.save(any(Boardgame.class))).thenReturn(testGame);

        Boardgame result = boardgameService.addBoardgame(testGame);

        assertNotNull(result);
        assertEquals("Catan", result.getName());
        verify(boardgameRepository, times(1)).findByNameIgnoreCase("Catan");
    }

    @Test
    void addBoardgame_DuplicateName_ShouldThrowException() {
        when(boardgameRepository.findByNameIgnoreCase("Catan")).thenReturn(Optional.of(testGame));

        assertThrows(ConflictException.class, () -> boardgameService.addBoardgame(testGame));
        verify(boardgameRepository, times(1)).findByNameIgnoreCase("Catan");
    }

    @Test
    void addBoardgame_No_Name_Failure() {
        Boardgame invalidGame = new Boardgame("", new BigDecimal("29.99"), true, 2, 5, "Adventure", null);
        assertThrows(BadRequestException.class, () -> boardgameService.addBoardgame(invalidGame));
    }

    @Test
    void updateBoardgame_ExistingId_ShouldUpdateGame() {
        Boardgame updatedGame = new Boardgame("Updated Catan", new BigDecimal("49.99"), true, 2, 5, "Adventure", null);

        when(boardgameRepository.findById(1L)).thenReturn(Optional.of(testGame));
        when(boardgameRepository.save(any(Boardgame.class))).thenReturn(updatedGame);

        Boardgame result = boardgameService.updateBoardgame(1L, updatedGame);

        assertNotNull(result);
        assertEquals("Updated Catan", result.getName());
        verify(boardgameRepository, times(1)).findById(1L);
    }

    @Test
    void updateBoardgame_NonExistingId_ShouldThrowException() {
        when(boardgameRepository.findById(99L)).thenReturn(Optional.empty());

        Boardgame updatedGame = new Boardgame("Updated Catan", new BigDecimal("49.99"), true, 2, 5, "Adventure", null);

        assertThrows(GameNotFoundException.class, () -> boardgameService.updateBoardgame(99L, updatedGame));
        verify(boardgameRepository, times(1)).findById(99L);
    }

    @Test
    void updateBoardgame_No_Name_Failure() {
        Boardgame invalidGame = new Boardgame("", new BigDecimal("29.99"), true, 2, 5, "Adventure", null);
        assertThrows(BadRequestException.class, () -> boardgameService.updateBoardgame(1L, invalidGame));
    }

    @Test
    void deleteBoardgame_ExistingId_ShouldDeleteGame() {
        when(boardgameRepository.existsById(1L)).thenReturn(true);
        doNothing().when(boardgameRepository).deleteById(1L);

        assertDoesNotThrow(() -> boardgameService.deleteBoardgame(1L));
        verify(boardgameRepository, times(1)).existsById(1L);
    }

    @Test
    void deleteBoardgame_NonExistingId_ShouldThrowException() {
        when(boardgameRepository.existsById(99L)).thenReturn(false);

        assertThrows(GameNotFoundException.class, () -> boardgameService.deleteBoardgame(99L));
        verify(boardgameRepository, times(1)).existsById(99L);
    }

    @Test
    void getFilteredBoardgames_ShouldFilterByGenre() {
        when(boardgameRepository.findAll()).thenReturn(List.of(testGame));
        List<Boardgame> result = boardgameService.getFilteredBoardgames("Strategy", null, null, null);
        assertEquals(1, result.size());
        assertEquals("Catan", result.get(0).getName());
    }

    @Test
    void getFilteredBoardgames_ShouldFilterByAvailability() {
        when(boardgameRepository.findAll()).thenReturn(List.of(testGame));
        List<Boardgame> result = boardgameService.getFilteredBoardgames(null, true, null, null);
        assertEquals(1, result.size());
    }

    @Test
    void getFilteredBoardgames_ShouldFilterByMinPlayers() {
        when(boardgameRepository.findAll()).thenReturn(List.of(testGame));
        List<Boardgame> result = boardgameService.getFilteredBoardgames(null, null, 3, null);
        assertEquals(1, result.size());
    }

    @Test
    void getFilteredBoardgames_ShouldFilterByMaxPlayers() {
        when(boardgameRepository.findAll()).thenReturn(List.of(testGame));
        List<Boardgame> result = boardgameService.getFilteredBoardgames(null, null, null, 4);
        assertEquals(1, result.size());
    }

    @Test
    void getFilteredBoardgames_NoMatches_ShouldReturnEmptyList() {
        when(boardgameRepository.findAll()).thenReturn(List.of(testGame));
        List<Boardgame> result = boardgameService.getFilteredBoardgames("Adventure", null, null, null);
        assertTrue(result.isEmpty());
    }

    @Test
    void getSpecialBoardgame_ExistingId_ShouldReturnBoardgame() {
        when(boardgameRepository.findById(1L)).thenReturn(Optional.of(testGame));
        Boardgame result = boardgameService.getSpecialBoardgame(1);
        assertEquals("Catan", result.getName());
    }
    @Test
    void getSpecialBoardgame_TheepotId_ShouldThrowTeapotException() {
        assertThrows(TeapotException.class, () -> boardgameService.getSpecialBoardgame(418));
    }
}
