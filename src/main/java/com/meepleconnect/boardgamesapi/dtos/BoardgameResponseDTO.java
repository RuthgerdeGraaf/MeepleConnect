package com.meepleconnect.boardgamesapi.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BoardgameResponseDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer minPlayers;
    private Integer maxPlayers;
    private String genre;
    private Boolean available;
    private PublisherResponseDTO publisher;
}