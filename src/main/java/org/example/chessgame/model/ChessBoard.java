package org.example.chessgame.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Arrays;

@Embeddable
public class ChessBoard implements Serializable {
    private static final long serialVersionUID = 1L;
    private Square[][] board;
    private boolean whiteTurn;
    private String enPassantSquare;
    private int halfMoveClock;
    private int fullMoveNumber;
    private boolean whiteCanCastleKingSide;
    private boolean whiteCanCastleQueenSide;
    private boolean blackCanCastleKingSide;
    private boolean blackCanCastleQueenSide;

    public ChessBoard() {
        board = new Square[8][8];
        whiteTurn = true;
        enPassantSquare = "-";
        halfMoveClock = 0;
        fullMoveNumber = 1;
        whiteCanCastleKingSide = true;
        whiteCanCastleQueenSide = true;
        blackCanCastleKingSide = true;
        blackCanCastleQueenSide = true;
        initializeBoardAndPieces();
    }

    private void initializeBoardAndPieces() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Color color = (i + j) % 2 == 0 ? Color.BLACK : Color.WHITE;
                board[i][j] = new Square(color);
            }
        }

        initializeBlackPieces();
        initializeWhitePieces();
    }

    private void initializeBlackPieces() {
        for (int i = 0; i < 8; i++) {
            board[1][i].setPiece(new Pawn(Color.BLACK, "P"));
        }

        board[0][0].setPiece(new Rook(Color.BLACK, "R"));
        board[0][7].setPiece(new Rook(Color.BLACK, "R"));
        board[0][1].setPiece(new Knight(Color.BLACK, "N"));
        board[0][6].setPiece(new Knight(Color.BLACK, "N"));
        board[0][2].setPiece(new Bishop(Color.BLACK, "B"));
        board[0][5].setPiece(new Bishop(Color.BLACK, "B"));
        board[0][3].setPiece(new Queen(Color.BLACK, "Q"));
        board[0][4].setPiece(new King(Color.BLACK, "K"));
    }

    private void initializeWhitePieces() {
        for (int i = 0; i < 8; i++) {
            board[6][i].setPiece(new Pawn(Color.WHITE, "p"));
        }

        board[7][0].setPiece(new Rook(Color.WHITE, "r"));
        board[7][7].setPiece(new Rook(Color.WHITE, "r"));
        board[7][1].setPiece(new Knight(Color.WHITE, "n"));
        board[7][6].setPiece(new Knight(Color.WHITE, "n"));
        board[7][2].setPiece(new Bishop(Color.WHITE, "b"));
        board[7][5].setPiece(new Bishop(Color.WHITE, "b"));
        board[7][3].setPiece(new Queen(Color.WHITE, "q"));
        board[7][4].setPiece(new King(Color.WHITE, "k"));
    }

    public boolean movePiece(int startRow, int startCol, int endRow, int endCol, Piece currentPlayer) {
        Piece piece = board[startRow][startCol].getPiece();
        if (piece != null && piece.getColor() == currentPlayer.getColor() && piece.isValidMove(startRow, startCol, endRow, endCol, board)) {
            // Castling logic
            if (piece instanceof King) {
                if (currentPlayer.getColor() == Color.WHITE) {
                    whiteCanCastleKingSide = false;
                    whiteCanCastleQueenSide = false;
                } else {
                    blackCanCastleKingSide = false;
                    blackCanCastleQueenSide = false;
                }
            } else if (piece instanceof Rook) {
                if (currentPlayer.getColor() == Color.WHITE) {
                    if (startRow == 7 && startCol == 0) {
                        whiteCanCastleQueenSide = false;
                    } else if (startRow == 7 && startCol == 7) {
                        whiteCanCastleKingSide = false;
                    }
                } else {
                    if (startRow == 0 && startCol == 0) {
                        blackCanCastleQueenSide = false;
                    } else if (startRow == 0 && startCol == 7) {
                        blackCanCastleKingSide = false;
                    }
                }
            }

            board[endRow][endCol].setPiece(piece);
            board[startRow][startCol].setPiece(null);

            // Pawn promotion logic
            if (piece instanceof Pawn && (endRow == 0 || endRow == 7)) {
                board[endRow][endCol].setPiece(new Queen(piece.getColor(), "Q"));
            }

            // En passant logic
            if (piece instanceof Pawn) {
                Pawn pawn = (Pawn) piece;
                if (Math.abs(startRow - endRow) == 2) {
                    enPassantSquare = (char)('a' + startCol) + (8 - (startRow + endRow) / 2) + "";
                } else {
                    enPassantSquare = "-";
                }
            } else {
                enPassantSquare = "-";
            }

            // Reset the halfmove clock if a pawn is moved or a capture is made
            if (piece instanceof Pawn || board[endRow][endCol].getPiece() != null) {
                halfMoveClock = 0;
            } else {
                halfMoveClock++;
            }


            whiteTurn = !whiteTurn;
            if (!whiteTurn) fullMoveNumber++;
            return true;
        }
        return false;
    }

    public boolean canWhiteCastleKingSide() {
        return whiteCanCastleKingSide;
    }

    public boolean canWhiteCastleQueenSide() {
        return whiteCanCastleQueenSide;
    }

    public boolean canBlackCastleKingSide() {
        return blackCanCastleKingSide;
    }

    public boolean canBlackCastleQueenSide() {
        return blackCanCastleQueenSide;
    }

    public Square[][] getBoard() {
        return board;
    }

    public void setBoard(Square[][] board) {
        this.board = board;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public String getEnPassantSquare() {
        return enPassantSquare;
    }

    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    public int getFullMoveNumber() {
        return fullMoveNumber;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(board);
    }
}
