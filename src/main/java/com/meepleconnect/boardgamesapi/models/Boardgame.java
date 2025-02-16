package com.meepleconnect.boardgamesapi.models;

import jakarta.persistence.*;
import java.math.BigDecimal;  // ✅ Importeer BigDecimal

@Entity
public class Boardgame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private int expansions;
    private boolean available;
    private int minPlayers;
    private int maxPlayers;
    private String genre;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    public Boardgame() {}

    public Boardgame(String name, BigDecimal price, int expansions, boolean available, 
                     int minPlayers, int maxPlayers, String genre, Publisher publisher) {
        this.name = name;
        this.price = price;
        this.expansions = expansions;
        this.available = available;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.genre = genre;
        this.publisher = publisher;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getExpansions() { return expansions; }
    public boolean isAvailable() { return available; }
    public int getMinPlayers() { return minPlayers; }
    public int getMaxPlayers() { return maxPlayers; }
    public String getGenre() { return genre; }
    public Publisher getPublisher() { return publisher; }

    public void setName(String name) { this.name = name; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setExpansions(int expansions) { this.expansions = expansions; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setMinPlayers(int minPlayers) { this.minPlayers = minPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setPublisher(Publisher publisher) { this.publisher = publisher; }
}
