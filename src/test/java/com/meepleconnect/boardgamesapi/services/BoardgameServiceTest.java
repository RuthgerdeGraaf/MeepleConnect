package com.meepleconnect.boardgamesapi.services;

import com.meepleconnect.boardgamesapi.exceptions.BadRequestException;
import com.meepleconnect.boardgamesapi.exceptions.ConflictException;
import com.meepleconnect.boardgamesapi.exceptions.GameNotFoundException;
import com.meepleconnect.boardgamesapi.exceptions.TeapotException;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.models.Publisher;
import com.meepleconnect.boardgamesapi.repositories.BoardgameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BoardgameServiceTest {

    @Mock
    private BoardgameRepository boardgameRepository;

    @InjectMocks
    private BoardgameService boardgameService;

    private Publisher testPublisher;
    private Boardgame testBoardgame;
    private Boardgame testBoardgame2;

    @BeforeEach
    void setUp() {
        testPublisher = new Publisher();
        testPublisher.setName("Test Publisher");
        testPublisher.setCountryOfOrigin("Netherlands");
        testPublisher.setFounded(2020);
        testPublisher.setIndie(true);

        testBoardgame = new Boardgame();
        testBoardgame.setId(1L);
        testBoardgame.setName("Test Game");
        testBoardgame.setPrice(new BigDecimal("29.99"));
        testBoardgame.setAvailable(true);
        testBoardgame.setMinPlayers(2);
        testBoardgame.setMaxPlayers(4);
        testBoardgame.setGenre("Strategy");
        testBoardgame.setPublisher(testPublisher);

        testBoardgame2 = new Boardgame();
        testBoardgame2.setId(2L);
        testBoardgame2.setName("Second Game");
        testBoardgame2.setPrice(new BigDecimal("39.99"));
        testBoardgame2.setAvailable(false);
        testBoardgame2.setMinPlayers(1);
        testBoardgame2.setMaxPlayers(6);
        testBoardgame2.setGenre("Family");
        testBoardgame2.setPublisher(testPublisher);
    }

    @Test
    void getAllBoardgames_ShouldReturnAllBoardgames() {
        List<Boardgame> expectedGames = List.of(testBoardgame, testBoardgame2);
        when(boardgameRepository.findAll()).thenReturn(expectedGames);

        List<Boardgame> result = boardgameService.getAllBoardgames();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testBoardgame, testBoardgame2);
        verify(boardgameRepository, times(1)).findAll();
    }

    @Test
    void getBoardgamesByGenre_ShouldReturnBoardgamesOfSpecificGenre() {
        List<Boardgame> strategyGames = List.of(testBoardgame);
        when(boardgameRepository.findByGenreIgnoreCase("Strategy")).thenReturn(strategyGames);

        List<Boardgame> result = boardgameService.getBoardgamesByGenre("Strategy");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getGenre()).isEqualTo("Strategy");
        verify(boardgameRepository, times(1)).findByGenreIgnoreCase("Strategy");
    }

    @Test
    void getBoardgameById_WithValidId_ShouldReturnBoardgame() {
        when(boardgameRepository.findById(1L)).thenReturn(Optional.of(testBoardgame));

        Boardgame result = boardgameService.getBoardgameById(1L);

        assertThat(result).isEqualTo(testBoardgame);
        verify(boardgameRepository, times(1)).findById(1L);
    }

    @Test
    void getBoardgameById_WithInvalidId_ShouldThrowGameNotFoundException() {
        when(boardgameRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boardgameService.getBoardgameById(999L))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage("Boardgame with ID 999 not found.");

        verify(boardgameRepository, times(1)).findById(999L);
    }

    @Test
    void addBoardgame_WithValidBoardgame_ShouldSaveBoardgame() {
        when(boardgameRepository.findByNameIgnoreCase("New Game")).thenReturn(Optional.empty());
        when(boardgameRepository.save(any(Boardgame.class))).thenReturn(testBoardgame);

        Boardgame newGame = new Boardgame();
        newGame.setName("New Game");
        newGame.setPrice(new BigDecimal("49.99"));

        Boardgame result = boardgameService.addBoardgame(newGame);

        assertThat(result).isEqualTo(testBoardgame);
        verify(boardgameRepository, times(1)).findByNameIgnoreCase("New Game");
        verify(boardgameRepository, times(1)).save(newGame);
    }

    @Test
    void addBoardgame_WithEmptyName_ShouldThrowBadRequestException() {
        Boardgame invalidGame = new Boardgame();
        invalidGame.setName("");

        assertThatThrownBy(() -> boardgameService.addBoardgame(invalidGame))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Name of boardgame can't be empty.");

        verify(boardgameRepository, never()).save(any());
    }

    @Test
    void addBoardgame_WithNullName_ShouldThrowBadRequestException() {
        Boardgame invalidGame = new Boardgame();
        invalidGame.setName(null);

        assertThatThrownBy(() -> boardgameService.addBoardgame(invalidGame))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Name of boardgame can't be empty.");

        verify(boardgameRepository, never()).save(any());
    }

    @Test
    void addBoardgame_WithExistingName_ShouldThrowConflictException() {
        when(boardgameRepository.findByNameIgnoreCase("Test Game")).thenReturn(Optional.of(testBoardgame));

        Boardgame duplicateGame = new Boardgame();
        duplicateGame.setName("Test Game");

        assertThatThrownBy(() -> boardgameService.addBoardgame(duplicateGame))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Boardagme with name 'Test Game' already exists.");

        verify(boardgameRepository, times(1)).findByNameIgnoreCase("Test Game");
        verify(boardgameRepository, never()).save(any());
    }

    @Test
    void updateBoardgame_WithValidData_ShouldUpdateBoardgame() {
        Boardgame updatedData = new Boardgame();
        updatedData.setName("Updated Game");
        updatedData.setPrice(new BigDecimal("59.99"));
        updatedData.setAvailable(false);
        updatedData.setMinPlayers(3);
        updatedData.setMaxPlayers(5);
        updatedData.setGenre("Adventure");

        when(boardgameRepository.findById(1L)).thenReturn(Optional.of(testBoardgame));
        when(boardgameRepository.save(any(Boardgame.class))).thenReturn(testBoardgame);

        Boardgame result = boardgameService.updateBoardgame(1L, updatedData);

        assertThat(result).isEqualTo(testBoardgame);
        verify(boardgameRepository, times(1)).findById(1L);
        verify(boardgameRepository, times(1)).save(testBoardgame);
    }

    @Test
    void updateBoardgame_WithInvalidId_ShouldThrowGameNotFoundException() {
        Boardgame updatedData = new Boardgame();
        updatedData.setName("Updated Game");

        when(boardgameRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boardgameService.updateBoardgame(999L, updatedData))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage("Boardgame with ID 999 not found.");

        verify(boardgameRepository, times(1)).findById(999L);
        verify(boardgameRepository, never()).save(any());
    }

    @Test
    void updateBoardgame_WithEmptyName_ShouldThrowBadRequestException() {
        Boardgame invalidData = new Boardgame();
        invalidData.setName("");

        assertThatThrownBy(() -> boardgameService.updateBoardgame(1L, invalidData))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Name of boardgame can't be empty.");

        verify(boardgameRepository, never()).findById(anyLong());
        verify(boardgameRepository, never()).save(any());
    }

    @Test
    void updateBoardgame_WithNullName_ShouldThrowBadRequestException() {
        Boardgame invalidData = new Boardgame();
        invalidData.setName(null);

        assertThatThrownBy(() -> boardgameService.updateBoardgame(1L, invalidData))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Name of boardgame can't be empty.");

        verify(boardgameRepository, never()).findById(anyLong());
        verify(boardgameRepository, never()).save(any());
    }

    @Test
    void deleteBoardgame_WithValidId_ShouldDeleteBoardgame() {
        when(boardgameRepository.existsById(1L)).thenReturn(true);

        boardgameService.deleteBoardgame(1L);

        verify(boardgameRepository, times(1)).existsById(1L);
        verify(boardgameRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBoardgame_WithInvalidId_ShouldThrowGameNotFoundException() {
        when(boardgameRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> boardgameService.deleteBoardgame(999L))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage("Boardgame with ID 999 not found.");

        verify(boardgameRepository, times(1)).existsById(999L);
        verify(boardgameRepository, never()).deleteById(anyLong());
    }

    @Test
    void getSpecialBoardgame_WithTeapotId_ShouldThrowTeapotException() {
        assertThatThrownBy(() -> boardgameService.getSpecialBoardgame(418))
                .isInstanceOf(TeapotException.class)
                .hasMessage("This boardgame is a teapot!");

        verify(boardgameRepository, never()).findById(anyLong());
    }

    @Test
    void getSpecialBoardgame_WithValidId_ShouldReturnBoardgame() {
        when(boardgameRepository.findById(1L)).thenReturn(Optional.of(testBoardgame));

        Boardgame result = boardgameService.getSpecialBoardgame(1);

        assertThat(result).isEqualTo(testBoardgame);
        verify(boardgameRepository, times(1)).findById(1L);
    }

    @Test
    void getSpecialBoardgame_WithInvalidId_ShouldThrowGameNotFoundException() {
        when(boardgameRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boardgameService.getSpecialBoardgame(999))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage("Boardgame with ID 999 not found.");

        verify(boardgameRepository, times(1)).findById(999L);
    }

    @Test
    void getFilteredBoardgames_WithGenreFilter_ShouldReturnFilteredGames() {
        List<Boardgame> allGames = List.of(testBoardgame, testBoardgame2);
        when(boardgameRepository.findAll()).thenReturn(allGames);

        List<Boardgame> result = boardgameService.getFilteredBoardgames("Strategy", null, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getGenre()).isEqualTo("Strategy");
        verify(boardgameRepository, times(1)).findAll();
    }

    @Test
    void getFilteredBoardgames_WithAvailabilityFilter_ShouldReturnFilteredGames() {
        List<Boardgame> allGames = List.of(testBoardgame, testBoardgame2);
        when(boardgameRepository.findAll()).thenReturn(allGames);

        List<Boardgame> result = boardgameService.getFilteredBoardgames(null, true, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).isAvailable()).isTrue();
        verify(boardgameRepository, times(1)).findAll();
    }

    @Test
    void getFilteredBoardgames_WithMinPlayersFilter_ShouldReturnFilteredGames() {
        List<Boardgame> allGames = List.of(testBoardgame, testBoardgame2);
        when(boardgameRepository.findAll()).thenReturn(allGames);

        List<Boardgame> result = boardgameService.getFilteredBoardgames(null, null, 2, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMinPlayers()).isEqualTo(2);
        verify(boardgameRepository, times(1)).findAll();
    }

    @Test
    void getFilteredBoardgames_WithMaxPlayersFilter_ShouldReturnFilteredGames() {
        List<Boardgame> allGames = List.of(testBoardgame, testBoardgame2);
        when(boardgameRepository.findAll()).thenReturn(allGames);

        List<Boardgame> result = boardgameService.getFilteredBoardgames(null, null, null, 4);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMaxPlayers()).isEqualTo(4);
        verify(boardgameRepository, times(1)).findAll();
    }

    @Test
    void getFilteredBoardgames_WithMultipleFilters_ShouldReturnFilteredGames() {
        List<Boardgame> allGames = List.of(testBoardgame, testBoardgame2);
        when(boardgameRepository.findAll()).thenReturn(allGames);

        List<Boardgame> result = boardgameService.getFilteredBoardgames("Strategy", true, 2, 4);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testBoardgame);
        verify(boardgameRepository, times(1)).findAll();
    }

    @Test
    void getFilteredBoardgames_WithNoFilters_ShouldReturnAllGames() {
        List<Boardgame> allGames = List.of(testBoardgame, testBoardgame2);
        when(boardgameRepository.findAll()).thenReturn(allGames);

        List<Boardgame> result = boardgameService.getFilteredBoardgames(null, null, null, null);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testBoardgame, testBoardgame2);
        verify(boardgameRepository, times(1)).findAll();
    }

    @Test
    void getTotalBoardgamesCount_ShouldReturnTotalCount() {
        when(boardgameRepository.count()).thenReturn(5L);

        long result = boardgameService.getTotalBoardgamesCount();

        assertThat(result).isEqualTo(5L);
        verify(boardgameRepository, times(1)).count();
    }

    @Test
    void getAvailableBoardgamesCount_ShouldReturnAvailableCount() {
        List<Boardgame> allGames = List.of(testBoardgame, testBoardgame2);
        when(boardgameRepository.findAll()).thenReturn(allGames);

        long result = boardgameService.getAvailableBoardgamesCount();

        assertThat(result).isEqualTo(1L);
        verify(boardgameRepository, times(1)).findAll();
    }

    @Test
    void getPopularBoardgames_ShouldReturnPopularGamesMap() {
        List<Boardgame> allGames = List.of(testBoardgame, testBoardgame2);
        when(boardgameRepository.findAll()).thenReturn(allGames);

        Map<String, Object> result = boardgameService.getPopularBoardgames();

        assertThat(result).containsKey("popularGames");
        List<Map<String, Object>> popularGames = (List<Map<String, Object>>) result.get("popularGames");
        assertThat(popularGames).hasSize(2);

        Map<String, Object> firstGame = popularGames.get(0);
        assertThat(firstGame.get("id")).isEqualTo(1L);
        assertThat(firstGame.get("name")).isEqualTo("Test Game");
        assertThat(firstGame.get("genre")).isEqualTo("Strategy");
        assertThat(firstGame.get("popularityScore")).isEqualTo(100);

        verify(boardgameRepository, times(1)).findAll();
    }
}