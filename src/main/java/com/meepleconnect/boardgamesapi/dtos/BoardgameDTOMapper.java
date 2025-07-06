package com.meepleconnect.boardgamesapi.dtos;

import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.models.Publisher;
import com.meepleconnect.boardgamesapi.repositories.PublisherRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BoardgameDTOMapper {

    private final PublisherRepository publisherRepository;

    public BoardgameDTOMapper(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public BoardgameResponseDTO toResponseDTO(Boardgame boardgame) {
        BoardgameResponseDTO dto = new BoardgameResponseDTO();
        dto.setId(boardgame.getId());
        dto.setName(boardgame.getName());
        dto.setPrice(boardgame.getPrice());
        dto.setMinPlayers(boardgame.getMinPlayers());
        dto.setMaxPlayers(boardgame.getMaxPlayers());
        dto.setGenre(boardgame.getGenre());
        dto.setAvailable(boardgame.isAvailable());

        if (boardgame.getPublisher() != null) {
            PublisherResponseDTO publisherDTO = new PublisherResponseDTO();
            publisherDTO.setId(boardgame.getPublisher().getId());
            publisherDTO.setName(boardgame.getPublisher().getName());
            publisherDTO.setCountryOfOrigin(boardgame.getPublisher().getCountryOfOrigin());
            publisherDTO.setFounded(boardgame.getPublisher().getFounded());
            publisherDTO.setIsIndie(boardgame.getPublisher().isIndie());
            dto.setPublisher(publisherDTO);
        }

        return dto;
    }

    public Boardgame toEntity(BoardgameRequestDTO dto) {
        Boardgame boardgame = new Boardgame();
        boardgame.setName(dto.getName());
        boardgame.setPrice(dto.getPrice());
        boardgame.setMinPlayers(dto.getMinPlayers());
        boardgame.setMaxPlayers(dto.getMaxPlayers());
        boardgame.setGenre(dto.getGenre());
        boardgame.setAvailable(dto.getAvailable());

        if (dto.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(dto.getPublisherId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Publisher not found with ID: " + dto.getPublisherId()));
            boardgame.setPublisher(publisher);
        }

        return boardgame;
    }

    public List<BoardgameResponseDTO> toResponseDTOList(List<Boardgame> boardgames) {
        return boardgames.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}