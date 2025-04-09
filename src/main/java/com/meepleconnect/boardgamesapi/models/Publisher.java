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

    public Long getId() {
        return id;
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public void setFounded(int founded) {
        this.founded = founded;
    }

    public void setIndie(boolean indie) {
        isIndie = indie;
    }
}
