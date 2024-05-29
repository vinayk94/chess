package org.example.chessgame.repository;

import org.example.chessgame.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GameRepository extends JpaRepository<Game, Long> {
    // Custom query methods can be added here if needed
}
