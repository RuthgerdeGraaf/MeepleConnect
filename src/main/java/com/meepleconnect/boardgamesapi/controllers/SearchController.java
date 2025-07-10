package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.dtos.BoardgameResponseDTO;
import com.meepleconnect.boardgamesapi.dtos.BoardgameDTOMapper;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.services.BoardgameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final BoardgameService boardgameService;
    private final BoardgameDTOMapper boardgameDTOMapper;

    public SearchController(BoardgameService boardgameService, BoardgameDTOMapper boardgameDTOMapper) {
        this.boardgameService = boardgameService;
        this.boardgameDTOMapper = boardgameDTOMapper;
    }

    @PostMapping("/boardgames")
    public ResponseEntity<List<BoardgameResponseDTO>> searchBoardgames(
            @RequestBody Map<String, Object> searchCriteria) {
        String genre = (String) searchCriteria.get("genre");
        Boolean available = (Boolean) searchCriteria.get("available");
        Integer minPlayers = (Integer) searchCriteria.get("minPlayers");
        Integer maxPlayers = (Integer) searchCriteria.get("maxPlayers");
        Double maxPrice = (Double) searchCriteria.get("maxPrice");

        List<Boardgame> boardgames = boardgameService.getFilteredBoardgames(genre, available, minPlayers, maxPlayers);

                // Filter op prijs als opgegeven
        if (maxPrice != null) {
            boardgames = boardgames.stream()
                .filter(game -> game.getPrice().doubleValue() <= maxPrice)
                .toList();
        }

        return ResponseEntity.ok(boardgameDTOMapper.toResponseDTOList(boardgames));
    }

    @PostMapping("/boardgames/advanced")
    public ResponseEntity<List<BoardgameResponseDTO>> advancedSearch(
            @RequestBody Map<String, Object> advancedCriteria) {
        String name = (String) advancedCriteria.get("name");
        String genre = (String) advancedCriteria.get("genre");
        Boolean available = (Boolean) advancedCriteria.get("available");
        Integer minPlayers = (Integer) advancedCriteria.get("minPlayers");
        Integer maxPlayers = (Integer) advancedCriteria.get("maxPlayers");
        Double minPrice = (Double) advancedCriteria.get("minPrice");
        Double maxPrice = (Double) advancedCriteria.get("maxPrice");

        List<Boardgame> boardgames = boardgameService.getAllBoardgames();

        // Pas filters toe
        if (name != null && !name.trim().isEmpty()) {
            boardgames = boardgames.stream()
                    .filter(game -> game.getName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
        }

        if (genre != null) {
            boardgames = boardgames.stream()
                    .filter(game -> game.getGenre().equalsIgnoreCase(genre))
                    .toList();
        }

        if (available != null) {
            boardgames = boardgames.stream()
                    .filter(game -> game.isAvailable() == available)
                    .toList();
        }

        if (minPlayers != null) {
            boardgames = boardgames.stream()
                    .filter(game -> game.getMinPlayers() >= minPlayers)
                    .toList();
        }

        if (maxPlayers != null) {
            boardgames = boardgames.stream()
                    .filter(game -> game.getMaxPlayers() <= maxPlayers)
                    .toList();
        }

                if (minPrice != null) {
            boardgames = boardgames.stream()
                .filter(game -> game.getPrice().doubleValue() >= minPrice)
                .toList();
        }
        
        if (maxPrice != null) {
            boardgames = boardgames.stream()
                .filter(game -> game.getPrice().doubleValue() <= maxPrice)
                .toList();
        }

        return ResponseEntity.ok(boardgameDTOMapper.toResponseDTOList(boardgames));
    }
}