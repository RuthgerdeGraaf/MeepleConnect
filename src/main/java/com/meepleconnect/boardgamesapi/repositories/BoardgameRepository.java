package com.meepleconnect.boardgamesapi.repositories;

import com.meepleconnect.boardgamesapi.models.Boardgame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardgameRepository extends JpaRepository<Boardgame, Long> {

    List<Boardgame> findByGenreIgnoreCase(String genre);

    Optional<Boardgame> findByNameIgnoreCase(String name);
}
