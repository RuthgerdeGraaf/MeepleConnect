package com.meepleconnect.boardgamesapi.services;

import com.meepleconnect.boardgamesapi.exceptions.BadRequestException;
import com.meepleconnect.boardgamesapi.exceptions.ConflictException;
import com.meepleconnect.boardgamesapi.exceptions.GameNotFoundException;
import com.meepleconnect.boardgamesapi.exceptions.TeapotException;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.repositories.BoardgameRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BoardgameService {

    private final BoardgameRepository boardgameRepository;

    public BoardgameService(BoardgameRepository boardgameRepository) {
        this.boardgameRepository = boardgameRepository;
    }

    public List<Boardgame> getAllBoardgames() {
        return boardgameRepository.findAll();
    }

    public List<Boardgame> getBoardgamesByGenre(String genre) {
        return boardgameRepository.findByGenreIgnoreCase(genre);
    }

    public Boardgame getBoardgameById(Long id) {
        return boardgameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Bordspel met ID " + id + " niet gevonden."));
    }

    public Boardgame addBoardgame(Boardgame boardgame) {
        if (boardgame.getName() == null || boardgame.getName().trim().isEmpty()) {
            throw new BadRequestException("Naam van het bordspel mag niet leeg zijn.");
        }
        
        Optional<Boardgame> existingBoardgame = boardgameRepository.findByNameIgnoreCase(boardgame.getName());
        if (existingBoardgame.isPresent()) {
            throw new ConflictException("Bordspel met de naam '" + boardgame.getName() + "' bestaat al.");
        }
        return boardgameRepository.save(boardgame);
    }

    public Boardgame updateBoardgame(Long id, Boardgame updatedBoardgame) {
        if (updatedBoardgame.getName() == null || updatedBoardgame.getName().trim().isEmpty()) {
            throw new BadRequestException("Naam van het bordspel mag niet leeg zijn.");
        }
    
        return boardgameRepository.findById(id).map(existingBoardgame -> {
            existingBoardgame.setName(updatedBoardgame.getName());
            existingBoardgame.setPrice(updatedBoardgame.getPrice());
            existingBoardgame.setExpansions(updatedBoardgame.getExpansions());
            existingBoardgame.setAvailable(updatedBoardgame.isAvailable());
            existingBoardgame.setMinPlayers(updatedBoardgame.getMinPlayers());
            existingBoardgame.setMaxPlayers(updatedBoardgame.getMaxPlayers());
            existingBoardgame.setGenre(updatedBoardgame.getGenre());
            existingBoardgame.setPublisher(updatedBoardgame.getPublisher());
    
            return boardgameRepository.save(existingBoardgame);
        }).orElseThrow(() -> new GameNotFoundException("Bordspel met ID " + id + " niet gevonden."));
    }
    

    public void deleteBoardgame(Long id) {
        if (!boardgameRepository.existsById(id)) {
            throw new GameNotFoundException("Bordspel met ID " + id + " niet gevonden.");
        }
        boardgameRepository.deleteById(id);
    }
    
    public Boardgame getSpecialBoardgame(int id) {
    if (id == 418) {
        throw new TeapotException("Dit bordspel is een theepot!");
    }
    Long i = (long) id;
        return boardgameRepository.findById(i)
            .orElseThrow(() -> new GameNotFoundException("Bordspel met ID " + id + " niet gevonden."));
    }

    public List<Boardgame> getFilteredBoardgames(String genre, Boolean available, Integer minPlayers, Integer maxPlayers) {
        List<Boardgame> boardgames = boardgameRepository.findAll();

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

        return boardgames;
    }

}
