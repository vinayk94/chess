package org.example.chessgame.repository;

import org.example.chessgame.model.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameRepository extends CrudRepository<Game, UUID> {
    List<Game> findByHost_Name(@Param("hostName") String hostName);
}
