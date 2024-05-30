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
import java.util.UUID;

@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    //private final PlayerRepository playerRepository;
    private final PlayerService playerService ;

    @Autowired
    public GameService(GameRepository gameRepository, PlayerService playerService) {
        this.gameRepository = gameRepository;
        //this.playerRepository = playerRepository;
        this.playerService = playerService;
    }

    public Game createGame(Player host) {
        // Ensure the player is created or fetched
        Player savedHost = playerService.createOrFetchPlayer(host);

        // Create a new game with the host player
        Game game = new Game();
        game.setHost(savedHost);
        game.setBoard(initializeBoard());

        // Log the initial board state
        logger.info("Initial board state: " + Arrays.deepToString(game.getBoard()));

        Game savedGame = gameRepository.save(game);
        logger.debug("Created game with ID: {}", savedGame.getId());
        return savedGame;
    }


    public Game makeMove(UUID gameId, Move move) {
        // Retrieve the game from the repository
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));

        // Get the current board state
        String[][] board = game.getBoard();
        String from = move.getFrom();
        String to = move.getTo();

        // Translate chess notation to array indices
        int[] fromPosition = translateChessNotation(from);
        int[] toPosition = translateChessNotation(to);

        // Log the board state before the move for debugging
        logger.info("Board before move: " + Arrays.deepToString(board));
        logger.info("Moving piece from " + from + " (row: " + fromPosition[0] + ", col: " + fromPosition[1] + ") to " + to + " (row: " + toPosition[0] + ", col: " + toPosition[1] + ")");

        // Ensure that the piece from the 'from' position is moved to the 'to' position
        String piece = board[fromPosition[0]][fromPosition[1]];
        logger.info("Piece to move: " + piece);

        // Verify the piece is not null
        if (piece == null) {
            logger.error("No piece found at the source position!");
            throw new RuntimeException("No piece found at the source position!");
        }

        // Move the piece
        board[toPosition[0]][toPosition[1]] = piece;
        // Clear the source position
        board[fromPosition[0]][fromPosition[1]] = null;

        // Log the board state after the move for debugging
        logger.info("Board after move: " + Arrays.deepToString(board));

        // Save the updated game state
        game.setBoard(board);
        gameRepository.save(game);

        return game;
    }



    public Game getGame(UUID gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
    }

    private int[] translateChessNotation(String notation) {
        int col = notation.charAt(0) - 'a';
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
