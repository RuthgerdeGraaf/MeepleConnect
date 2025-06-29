package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.services.BoardgameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boardgames")
@CrossOrigin(origins = "*")
public class BoardgameController {

    private final BoardgameService boardgameService;

    public BoardgameController(BoardgameService boardgameService) {
        this.boardgameService = boardgameService;
    }

    @GetMapping
    public List<Boardgame> getAllBoardgames(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) Integer minPlayers,
            @RequestParam(required = false) Integer maxPlayers) {
        return boardgameService.getFilteredBoardgames(genre, available, minPlayers, maxPlayers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Boardgame> getBoardgameById(@PathVariable Long id) {
        return ResponseEntity.ok(boardgameService.getBoardgameById(id));
    }

    @PostMapping
    public ResponseEntity<Boardgame> addBoardgame(@Valid @RequestBody Boardgame boardgame) {
        Boardgame savedBoardgame = boardgameService.addBoardgame(boardgame);
        return ResponseEntity.status(201).body(savedBoardgame);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boardgame> updateBoardgame(@PathVariable Long id, @Valid @RequestBody Boardgame boardgame) {
        return ResponseEntity.ok(boardgameService.updateBoardgame(id, boardgame));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoardgame(@PathVariable Long id) {
        boardgameService.deleteBoardgame(id);
        return ResponseEntity.noContent().build();
    }
}
