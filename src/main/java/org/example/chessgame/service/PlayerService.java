package org.example.chessgame.service;

import org.example.chessgame.model.Player;
import org.example.chessgame.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player createOrFetchPlayer(Player player) {
        try {
            return playerRepository.save(player);
        } catch (DataIntegrityViolationException e) {
            Optional<Player> existingPlayer = playerRepository.findByName(player.getName());
            if (existingPlayer.isPresent()) {
                return existingPlayer.get();
            } else {
                throw e;
            }
        }
    }
}
