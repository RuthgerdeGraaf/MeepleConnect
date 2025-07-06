package com.meepleconnect.boardgamesapi.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class BoardgameRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "999.99", message = "Price cannot be higher than 999.99")
    private BigDecimal price;

    @NotNull(message = "Minimum number of players is required")
    @Min(value = 1, message = "Minimum number of players must be at least 1")
    @Max(value = 20, message = "Minimum number of players cannot be more than 20")
    private Integer minPlayers;

    @NotNull(message = "Maximum number of players is required")
    @Min(value = 1, message = "Maximum number of players must be at least 1")
    @Max(value = 20, message = "Maximum number of players cannot be more than 20")
    private Integer maxPlayers;

    @NotBlank(message = "Genre is required")
    @Size(min = 1, max = 50, message = "Genre must be between 1 and 50 characters")
    private String genre;

    @NotNull(message = "Availability is required")
    private Boolean available;

    @NotNull(message = "Publisher is required")
    private Long publisherId;
}