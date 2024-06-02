package org.example.chessgame.controller;

import org.example.chessgame.model.Game;
import org.example.chessgame.model.MoveRequest;
import org.example.chessgame.model.MoveResponse;
import org.example.chessgame.model.Player;
import org.example.chessgame.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/game")
@CrossOrigin(origins = "http://127.0.0.1:8081")  // Update the allowed origin to match your frontend URL
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
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
    public ResponseEntity<?> makeMove(@RequestBody MoveRequest moveRequest) {
        try {
            logger.info("Received move request for game ID: {}, from: {}, to: {}", moveRequest.getGameId(), moveRequest.getFrom(), moveRequest.getTo());
            logger.info("MoveRequest object: {}", moveRequest);
            logger.info("Move object: {}", moveRequest.getMove());
            MoveResponse moveResponse = gameService.makeMove(moveRequest.getGameId(), moveRequest.getMove());
            return ResponseEntity.ok(moveResponse);
        } catch (Exception e) {
            logger.error("Error making move", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/{gameId}")
    public ResponseEntity<?> getGame(@PathVariable UUID gameId) {
        try {
            Game game = gameService.getGame(gameId);
            return ResponseEntity.ok(game);
        } catch (Exception e) {
            logger.error("Error fetching game", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/host/{hostName}")
    public ResponseEntity<?> getGamesByHostName(@PathVariable String hostName) {
        try {
            logger.info("Received request to find games by host name: {}", hostName);
            List<Game> games = gameService.findGamesByHostName(hostName);
            return ResponseEntity.ok(games);
        } catch (Exception e) {
            logger.error("Error finding games by host name", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
