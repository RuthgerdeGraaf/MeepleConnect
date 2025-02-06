package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.services.BoardgameService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boardgames")
public class BoardgameController {

    private final BoardgameService boardgameService;

    public BoardgameController(BoardgameService boardgameService) {
        this.boardgameService = boardgameService;
    }

    @GetMapping
    public List<Boardgame> getAllBoardgames() {
        return boardgameService.getAllBoardgames();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Boardgame> getBoardgameById(@PathVariable Long id) {
        return ResponseEntity.ok(boardgameService.getBoardgameById(id));
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public ResponseEntity<Boardgame> addBoardgame(@RequestBody Boardgame boardgame) {
        return ResponseEntity.ok(boardgameService.addBoardgame(boardgame));
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity<Boardgame> updateBoardgame(@PathVariable Long id, @RequestBody Boardgame boardgame) {
        return ResponseEntity.ok(boardgameService.updateBoardgame(id, boardgame));
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoardgame(@PathVariable Long id) {
        boardgameService.deleteBoardgame(id);
        return ResponseEntity.noContent().build();
    }
}
