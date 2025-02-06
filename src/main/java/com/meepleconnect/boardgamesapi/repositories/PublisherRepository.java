package com.meepleconnect.boardgamesapi.repositories;

import com.meepleconnect.boardgamesapi.models.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    List<Publisher> findByCountryOfOrigin(String countryOfOrigin);
    Publisher findByName(String name);
