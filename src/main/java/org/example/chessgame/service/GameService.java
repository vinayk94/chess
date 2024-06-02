package org.example.chessgame.service;

import org.example.chessgame.converter.ChessBoardConverter;
import org.example.chessgame.model.*;
import org.example.chessgame.repository.GameRepository;
import org.example.chessgame.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameService(GameRepository gameRepository, PlayerService playerService, SimpMessagingTemplate messagingTemplate) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
        this.messagingTemplate = messagingTemplate;
    }

    public Game createGame(Player host) {
        logger.info("Attempting to create game with host: {}", host.getName());

        // Ensure the player is created or fetched
        Player savedHost = playerService.createOrFetchPlayer(host);

        // Create a new game with the host player
        Game game = new Game();
        game.setHost(savedHost);
        ChessBoard chessBoard = new ChessBoard();
        game.setBoard(chessBoard);

        // Convert ChessBoard to JSON string and log the size
        ChessBoardConverter converter = new ChessBoardConverter();
        String boardJson = converter.convertToDatabaseColumn(chessBoard);
        logger.debug("Size of board JSON: {}", boardJson.length());

        Game savedGame = null;
        try {
            savedGame = gameRepository.save(game);
            logger.debug("Created game with ID: {}", savedGame.getId());
        } catch (Exception e) {
            logger.error("Error creating game: ", e);
        }
        return savedGame;
    }

    public MoveResponse makeMove(UUID gameId, Move move) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));

        ChessBoard board = game.getBoard();
        String from = move.getFrom();
        String to = move.getTo();

        if (from == null || to == null) {
            throw new RuntimeException("Move positions cannot be null");
        }

        // Translate chess notation to array indices
        int[] fromPosition = translateChessNotation(from);
        int[] toPosition = translateChessNotation(to);

        // Log the positions and the board state
        logger.info("Moving piece from {} (row: {}, col: {}) to {} (row: {}, col: {})", from, fromPosition[0], fromPosition[1], to, toPosition[0], toPosition[1]);
        logger.info("Board before move: {}", board);

        if (fromPosition[0] < 0 || fromPosition[0] > 7 || fromPosition[1] < 0 || fromPosition[1] > 7 ||
                toPosition[0] < 0 || toPosition[0] > 7 || toPosition[1] < 0 || toPosition[1] > 7) {
            throw new RuntimeException("Invalid move positions");
        }

        Square fromSquare = board.getBoard()[fromPosition[0]][fromPosition[1]];
        Square toSquare = board.getBoard()[toPosition[0]][toPosition[1]];

        if (fromSquare == null || toSquare == null) {
            throw new RuntimeException("Invalid board positions: Square is null");
        }

        Piece piece = fromSquare.getPiece();
        logger.info("Piece to move: {}", piece);

        if (piece == null) {
            throw new RuntimeException("No piece found at the source position");
        }

        // Move the piece
        toSquare.setPiece(piece);
        fromSquare.setPiece(null);

        // Update and save the game state
        game.setBoard(board);
        gameRepository.save(game);

        // Generate the FEN string after the move
        String fen = generateFenFromBoard(board);

        // Send the move via WebSocket
        MoveResponse moveResponse = new MoveResponse(move.getFrom(), move.getTo(), true, fen);
        messagingTemplate.convertAndSend("/topic/moves", moveResponse);

        return moveResponse;
    }


    private String generateFenFromBoard(ChessBoard board) {
        StringBuilder fen = new StringBuilder();

        // Piece placement
        for (int i = 0; i < 8; i++) {
            int emptyCount = 0;
            for (int j = 0; j < 8; j++) {
                Square square = board.getBoard()[i][j];
                Piece piece = square.getPiece();
                if (piece == null) {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }
                    fen.append(pieceToFenChar(piece));
                }
            }
            if (emptyCount > 0) {
                fen.append(emptyCount);
            }
            if (i < 7) {
                fen.append('/');
            }
        }

        // Active color
        fen.append(' ');
        fen.append(board.isWhiteTurn() ? 'w' : 'b');  // Assuming you have a method isWhiteTurn()
        fen.append(' ');

        // Castling availability
        boolean castlingAvailable = false;
        if (board.canWhiteCastleKingSide()) {
            fen.append('K');
            castlingAvailable = true;
        }
        if (board.canWhiteCastleQueenSide()) {
            fen.append('Q');
            castlingAvailable = true;
        }
        if (board.canBlackCastleKingSide()) {
            fen.append('k');
            castlingAvailable = true;
        }
        if (board.canBlackCastleQueenSide()) {
            fen.append('q');
            castlingAvailable = true;
        }
        if (!castlingAvailable) {
            fen.append('-');
        }
        fen.append(' ');

        // En passant target square
        String enPassantSquare = board.getEnPassantSquare(); // Ensure this returns "-" if no en passant target square
        fen.append(enPassantSquare.isEmpty() ? "-" : enPassantSquare);
        fen.append(' ');

        // Halfmove clock and fullmove number
        fen.append(board.getHalfMoveClock());
        fen.append(' ');
        fen.append(board.getFullMoveNumber());

        return fen.toString();
    }

    private char pieceToFenChar(Piece piece) {
        char pieceChar = piece.getSymbol().charAt(0);
        return piece.getColor() == Color.WHITE ? Character.toUpperCase(pieceChar) : Character.toLowerCase(pieceChar);
    }


    public Game getGame(UUID gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
    }

    public List<Game> findGamesByHostName(String hostName) {
        logger.info("Finding games with host name: {}", hostName);
        return gameRepository.findByHost_Name(hostName);
    }

    private int[] translateChessNotation(String notation) {
        int col = notation.charAt(0) - 'a';
        int row = 8 - Character.getNumericValue(notation.charAt(1));
        return new int[]{row, col};
    }
}
