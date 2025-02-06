package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.repositories.BoardgameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/boardgames")
public class BoardgameController {

    private final BoardgameRepository boardgameRepository;

    public BoardgameController(BoardgameRepository boardgameRepository) {
        this.boardgameRepository = boardgameRepository;
    }

    @GetMapping
    public List<Boardgame> getAllBoardgames() {
        return boardgameRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Boardgame> getBoardgameById(@PathVariable Long id) {
        Optional<Boardgame> boardgame = boardgameRepository.findById(id);
        return boardgame.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/genre/{genre}")
    public List<Boardgame> getBoardgamesByGenre(@PathVariable String genre) {
        return boardgameRepository.findByGenre(genre);
    }

    @GetMapping("/available")
    public List<Boardgame> getAvailableBoardgames() {
        return boardgameRepository.findByAvailableTrue();
    }

    @PostMapping
    public ResponseEntity<Boardgame> addBoardgame(@RequestBody Boardgame boardgame) {
        Boardgame savedGame = boardgameRepository.save(boardgame);
        return ResponseEntity.ok(savedGame);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boardgame> updateBoardgame(@PathVariable Long id, @RequestBody Boardgame updatedGame) {
        return boardgameRepository.findById(id).map(existingGame -> {
            existingGame.setName(updatedGame.getName());
            existingGame.setPrice(updatedGame.getPrice());
            existingGame.setExpansions(updatedGame.getExpansions());
            existingGame.setAvailable(updatedGame.isAvailable());
            existingGame.setMinPlayers(updatedGame.getMinPlayers());
            existingGame.setMaxPlayers(updatedGame.getMaxPlayers());
            existingGame.setGenre(updatedGame.getGenre());
            existingGame.setPublisher(updatedGame.getPublisher());

            boardgameRepository.save(existingGame);
            return ResponseEntity.ok(existingGame);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoardgame(@PathVariable Long id) {
        if (boardgameRepository.existsById(id)) {
            boardgameRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
