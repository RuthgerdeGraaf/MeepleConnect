package com.meepleconnect.boardgamesapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "boardgames")
public class Boardgame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "999.99", message = "Price cannot be higher than 999.99")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @NotNull(message = "Minimum number of players is required")
    @Min(value = 1, message = "Minimum number of players must be at least 1")
    @Max(value = 20, message = "Minimum number of players cannot be more than 20")
    @Column(name = "min_players", nullable = false)
    private int minPlayers;
    
    @NotNull(message = "Maximum number of players is required")
    @Min(value = 1, message = "Maximum number of players must be at least 1")
    @Max(value = 20, message = "Maximum number of players cannot be more than 20")
    @Column(name = "max_players", nullable = false)
    private int maxPlayers;
    
    @NotBlank(message = "Genre is required")
    @Size(min = 1, max = 50, message = "Genre must be between 1 and 50 characters")
    @Column(name = "genre", nullable = false, length = 50)
    private String genre;
    
    @Column(name = "available", nullable = false)
    private boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = false)
    @NotNull(message = "Publisher is required")
    private Publisher publisher;

    public Boardgame() {}

    public Boardgame(String name, BigDecimal price, boolean available, 
                     int minPlayers, int maxPlayers, String genre, Publisher publisher) {
        this.name = name;
        this.price = price;
        this.available = available;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.genre = genre;
        this.publisher = publisher;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public boolean isAvailable() { return available; }
    public int getMinPlayers() { return minPlayers; }
    public int getMaxPlayers() { return maxPlayers; }
    public String getGenre() { return genre; }
    public Publisher getPublisher() { return publisher; }
    public void setId(Long id) { this.id = id; }

    public void setName(String name) { this.name = name; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setMinPlayers(int minPlayers) { this.minPlayers = minPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setPublisher(Publisher publisher) { this.publisher = publisher; }
}
