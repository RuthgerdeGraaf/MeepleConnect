package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.dtos.BoardgameDTOMapper;
import com.meepleconnect.boardgamesapi.dtos.BoardgameRequestDTO;
import com.meepleconnect.boardgamesapi.dtos.BoardgameResponseDTO;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.services.BoardgameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/boardgames")
@CrossOrigin(origins = "*")
public class BoardgameController {

    private final BoardgameService boardgameService;
    private final BoardgameDTOMapper boardgameDTOMapper;

    public BoardgameController(BoardgameService boardgameService, BoardgameDTOMapper boardgameDTOMapper) {
        this.boardgameService = boardgameService;
        this.boardgameDTOMapper = boardgameDTOMapper;
    }

    @GetMapping
    public List<BoardgameResponseDTO> getAllBoardgames(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) Integer minPlayers,
            @RequestParam(required = false) Integer maxPlayers) {
        List<Boardgame> boardgames = boardgameService.getFilteredBoardgames(genre, available, minPlayers, maxPlayers);
        return boardgameDTOMapper.toResponseDTOList(boardgames);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardgameResponseDTO> getBoardgameById(@PathVariable Long id) {
        Boardgame boardgame = boardgameService.getBoardgameById(id);
        return ResponseEntity.ok(boardgameDTOMapper.toResponseDTO(boardgame));
    }

    @PostMapping
    public ResponseEntity<BoardgameResponseDTO> addBoardgame(
            @Valid @RequestBody BoardgameRequestDTO boardgameRequestDTO) {
        Boardgame boardgame = boardgameDTOMapper.toEntity(boardgameRequestDTO);
        Boardgame savedBoardgame = boardgameService.addBoardgame(boardgame);
        URI location = URI.create("/api/boardgames/" + savedBoardgame.getId());
        return ResponseEntity.created(location).body(boardgameDTOMapper.toResponseDTO(savedBoardgame));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardgameResponseDTO> updateBoardgame(@PathVariable Long id,
            @Valid @RequestBody BoardgameRequestDTO boardgameRequestDTO) {
        Boardgame boardgame = boardgameDTOMapper.toEntity(boardgameRequestDTO);
        Boardgame updatedBoardgame = boardgameService.updateBoardgame(id, boardgame);
        return ResponseEntity.ok(boardgameDTOMapper.toResponseDTO(updatedBoardgame));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoardgame(@PathVariable Long id) {
        boardgameService.deleteBoardgame(id);
        return ResponseEntity.noContent().build();
    }
}
