package com.meepleconnect.boardgamesapi.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String countryOfOrigin;
    private int founded;
    private boolean isIndie;

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Boardgame> boardgames;

    public Publisher() {}

    public Publisher(String name, String countryOfOrigin, int founded, boolean isIndie) {
        this.name = name;
        this.countryOfOrigin = countryOfOrigin;
        this.founded = founded;
        this.isIndie = isIndie;
    }
}
