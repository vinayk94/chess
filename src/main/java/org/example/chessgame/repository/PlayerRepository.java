package org.example.chessgame.repository;

import org.example.chessgame.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends CrudRepository<Player, UUID> {
    Optional<Player> findByName(String name);
}