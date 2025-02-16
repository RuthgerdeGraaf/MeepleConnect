package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.services.BoardgameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<Boardgame> getAllBoardgames(@RequestParam(required = false) String genre) {
        if (genre != null) {
            return boardgameService.getBoardgamesByGenre(genre);
        }
        return boardgameService.getAllBoardgames();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Boardgame> getBoardgameById(@PathVariable Long id) {
        return ResponseEntity.ok(boardgameService.getBoardgameById(id));
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public ResponseEntity<Boardgame> addBoardgame(@Valid @RequestBody Boardgame boardgame) {
        Boardgame savedBoardgame = boardgameService.addBoardgame(boardgame);
        return ResponseEntity.status(201).body(savedBoardgame);  // HTTP 201 Created
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity<Boardgame> updateBoardgame(@PathVariable Long id, @Valid @RequestBody Boardgame boardgame) {
        return ResponseEntity.ok(boardgameService.updateBoardgame(id, boardgame));
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoardgame(@PathVariable Long id) {
        boardgameService.deleteBoardgame(id);
        return ResponseEntity.noContent().build();
    }
}
