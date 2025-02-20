package com.meepleconnect.boardgamesapi.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Boardgame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private int minPlayers;
    private int maxPlayers;
    private String genre;
    private boolean available;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
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
    public void setId(Long id) { this.id = id; } // ✅ Toegevoegde setter

    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public boolean isAvailable() { return available; }
    public int getMinPlayers() { return minPlayers; }
    public int getMaxPlayers() { return maxPlayers; }
    public String getGenre() { return genre; }
    public Publisher getPublisher() { return publisher; }

    public void setName(String name) { this.name = name; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setMinPlayers(int minPlayers) { this.minPlayers = minPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setPublisher(Publisher publisher) { this.publisher = publisher; }
}
