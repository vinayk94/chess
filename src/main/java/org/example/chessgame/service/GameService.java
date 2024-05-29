package org.example.chessgame.service;

import org.example.chessgame.model.Game;
import org.example.chessgame.model.Move;
import org.example.chessgame.model.Player;
import org.example.chessgame.repository.GameRepository;
import org.example.chessgame.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public GameService(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    public Game createGame(Player host) {
        Player savedHost = playerRepository.save(host);

        Game game = new Game();
        game.setHost(savedHost);
        //game.setOpponent(null); // Initially no opponent
        //game.setBoard(new String[8][8]); // Initialize board
        game.setBoard(initializeBoard());
        Game savedGame = gameRepository.save(game);
        logger.debug("Created game with ID: " + savedGame.getId());
        return savedGame;
    }

    public Game makeMove(Long gameId, Move move) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));

        // Ensure it's the correct player's turn
        // If white's turn, check the piece at 'from' is a white piece
        // If black's turn, check the piece at 'from' is a black piece
        // Toggle turn after move

        String[][] board = game.getBoard();
        String from = move.getFrom();
        String to = move.getTo();

        int fromX = from.charAt(0) - 'a';
        int fromY = 8 - Character.getNumericValue(from.charAt(1));

        int toX = to.charAt(0) - 'a';
        int toY = 8 - Character.getNumericValue(to.charAt(1));

        // Move the piece from source to destination
        board[toY][toX] = board[fromY][fromX];
        // Clear the source square
        board[fromY][fromX] = null;

        // Save the updated game state
        game.setBoard(board);
        gameRepository.save(game);

        return game;
    }

    public Game getGame(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
    }


    private int[] translateChessNotation(String notation) {
        // Map files to columns (a-h -> 0-7)
        int col = notation.charAt(0) - 'a';
        // Map ranks to rows (1-8 -> 7-0)
        int row = 8 - Character.getNumericValue(notation.charAt(1));
        return new int[]{row, col};
    }

    private String[][] initializeBoard() {
        return new String[][]{
                {"R", "N", "B", "Q", "K", "B", "N", "R"},
                {"P", "P", "P", "P", "P", "P", "P", "P"},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {"p", "p", "p", "p", "p", "p", "p", "p"},
                {"r", "n", "b", "q", "k", "b", "n", "r"}
        };
    }
}
