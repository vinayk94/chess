package org.example.chessgame.controller;

import org.example.chessgame.model.Game;
import org.example.chessgame.model.Move;
import org.example.chessgame.model.Player;
import org.example.chessgame.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/game")
public class GameController {


    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService){
        this.gameService = gameService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGame(@RequestBody Player host) {
        try {
            logger.info("Received request to create game with host: {}", host.getName());
            Game createdGame = gameService.createGame(host);
            logger.info("Game created with ID: {}", createdGame.getId());
            return ResponseEntity.ok(createdGame);
        } catch (Exception e) {
            logger.error("Error creating game", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/move")
    public ResponseEntity<Game> makeMove(@RequestParam Long gameId, @RequestBody Move move) {
        logger.info("Received move request for game ID: {}, from: {}, to: {}", gameId, move.getFrom(), move.getTo());
        Game updatedGame = gameService.makeMove(gameId, move);
        return ResponseEntity.ok(updatedGame);
    }



    @GetMapping("/{gameId}")
    public Game getGame(@PathVariable Long gameId) {
        return gameService.getGame(gameId);
    }
}
