package com.meepleconnect.boardgamesapi.services;

import com.meepleconnect.boardgamesapi.exceptions.ConflictException;
import com.meepleconnect.boardgamesapi.exceptions.GameNotFoundException;
import com.meepleconnect.boardgamesapi.exceptions.TeapotException;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.repositories.BoardgameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BoardgameServiceTest {

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
    void getSpecialBoardgame_ShouldThrowTeapotException() {
        BoardgameService boardgameService = new BoardgameService(boardgameRepository);

        TeapotException exception = assertThrows(TeapotException.class, () -> {
        boardgameService.getSpecialBoardgame(418);
        verify(boardgameService , times(1)).getSpecialBoardgame(418);
    });

    assertEquals("Dit bordspel is een theepot!", exception.getMessage());
}

}
