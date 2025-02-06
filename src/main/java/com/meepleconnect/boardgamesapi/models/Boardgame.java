package com.meepleconnect.boardgamesapi.models;

import jakarta.persistence.*;

@Entity
public class Boardgame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private int expansions;
    private boolean available;
    private int minPlayers;
    private int maxPlayers;
    private String genre;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;

    public Boardgame() {}

    public Boardgame(String name, double price, int expansions, boolean available, int minPlayers, int maxPlayers, String genre, Publisher publisher) {
        this.name = name;
        this.price = price;
        this.expansions = expansions;
        this.available = available;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.genre = genre;
        this.publisher = publisher;
    }
}