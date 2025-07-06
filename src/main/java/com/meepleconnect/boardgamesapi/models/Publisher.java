package com.meepleconnect.boardgamesapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "publishers")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @NotBlank(message = "Country of origin is required")
    @Size(min = 1, max = 50, message = "Country must be between 1 and 50 characters")
    @Column(name = "country_of_origin", nullable = false, length = 50)
    private String countryOfOrigin;

    @NotNull(message = "Founded year is required")
    @Min(value = 1800, message = "Founded year must be after 1800")
    @Max(value = 2024, message = "Founded year cannot be in the future")
    @Column(name = "founded", nullable = false)
    private int founded;

    @Column(name = "is_indie", nullable = false)
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
