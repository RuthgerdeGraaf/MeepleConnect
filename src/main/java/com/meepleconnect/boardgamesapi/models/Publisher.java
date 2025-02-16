package com.meepleconnect.boardgamesapi.models;

import jakarta.persistence.*;

@Entity
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String countryOfOrigin;
    private int founded;
    private boolean isIndie;

    public String getName() {
        return name;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public int getFounded() {
        return founded;
    }

    public boolean isIndie() {
        return isIndie;
    }
}
