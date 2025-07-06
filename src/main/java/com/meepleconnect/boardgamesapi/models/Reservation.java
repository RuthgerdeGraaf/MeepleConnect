package com.meepleconnect.boardgamesapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meepleconnect.boardgamesapi.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    @NotNull(message = "Customer is required")
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardgame_id", nullable = false)
    @JsonIgnore
    @NotNull(message = "Boardgame is required")
    private Boardgame boardgame;

    @NotNull(message = "Reservation date is required")
    @Future(message = "Reservation date must be in the future")
    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    @NotNull(message = "Number of participants is required")
    @Min(value = 1, message = "Number of participants must be at least 1")
    @Max(value = 20, message = "Number of participants cannot be more than 20")
    @Column(name = "participant_count", nullable = false)
    private int participantCount;

    @Size(max = 500, message = "Notes cannot contain more than 500 characters")
    @Column(name = "notes", length = 500)
    private String notes;

    public Reservation() {
    }

    public Reservation(User customer, Boardgame boardgame, LocalDate reservationDate, int participantCount,
            String notes) {
        this.customer = customer;
        this.boardgame = boardgame;
        this.reservationDate = reservationDate;
        this.participantCount = participantCount;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Boardgame getBoardgame() {
        return boardgame;
    }

    public void setBoardgame(Boardgame boardgame) {
        this.boardgame = boardgame;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
