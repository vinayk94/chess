package org.example.chessgame.repository;

import org.example.chessgame.model.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameRepository extends CrudRepository<Game, UUID> {
}
