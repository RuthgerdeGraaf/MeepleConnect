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
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getExpansions() {
        return expansions;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getGenre() {
        return genre;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setExpansions(int expansions) {
        this.expansions = expansions;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
}
