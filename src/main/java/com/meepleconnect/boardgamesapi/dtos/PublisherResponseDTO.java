package com.meepleconnect.boardgamesapi.dtos;

import lombok.Data;

@Data
public class PublisherResponseDTO {
    private Long id;
    private String name;
    private String countryOfOrigin;
    private Integer founded;
    private Boolean isIndie;
}