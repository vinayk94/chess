import $ from 'jquery';
import { Chess } from 'chess.js';
import 'chessboard-js';
import 'chessboard-css';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

$(document).ready(function() {
    console.log("Document is ready");

    var gameId = '';
    var chess = new Chess();
    console.log("Chess game initialized:", chess);

    var board = Chessboard('board', {
        draggable: true,
        dropOffBoard: 'snapback',
        position: 'start',
        pieceTheme: 'img/chesspieces/wikipedia/{piece}.svg',
        onDragStart: function (source, piece, position, orientation) {
            console.log("Drag started");
            if (chess.isGameOver() ||
                (chess.turn() === 'w' && piece.search(/^b/) !== -1) ||
                (chess.turn() === 'b' && piece.search(/^w/) !== -1)) {
                return false;
            }
        },
        onDrop: function(source, target) {
            console.log("Piece dropped from", source, "to", target);
            if (source === target) {
                return 'snapback';
            }
            var move = chess.move({
                from: source,
                to: target,
                promotion: 'q'  // Assuming promotion to queen for simplicity
            });

            if (move === null) return 'snapback';

            $.ajax({
                url: `http://localhost:8080/game/move`,
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ gameId: gameId, from: source, to: target }),  // Correct the data format
                success: function(response) {
                    console.log("Move made successfully on the server:", response);
                    console.log("Response properties:", response.from, response.to, response.valid, response.fen);

                    // Log the chess instance state before making the move
                    console.log("Chess instance before move:", chess.fen());
                    console.log("Chess instance valid moves:", chess.moves());

                    chess.reset();
                    try {
                        const testChess = new Chess(response.fen);
                        console.log("FEN loaded successfully:", response.fen);
                        chess = testChess;
                        board.position(chess.fen());
                    } catch (e) {
                        console.error("Invalid FEN received from server:", response.fen, e);
                    }

                    // Log the chess instance state after making the move
                    console.log("Chess instance after move:", chess.fen());
                    console.log("Chess instance valid moves:", chess.moves());
                },
                error: function(xhr, status, error) {
                    console.error("Move error:", error);
                    board.position(chess.fen(), false);
                }
            });
        },
        onSnapEnd: function () {
            console.log("Snap end");
            board.position(chess.fen());
        }
    });

    var socket = new SockJS('http://localhost:8080/chess-websocket');
    var stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/moves', function(message) {
            var move = JSON.parse(message.body);
            console.log("Received move from WebSocket:", move);
            console.log("Move properties:", move.from, move.to, move.fen);

            // Log the chess instance state before making the move
            console.log("Chess instance before move:", chess.fen());
            console.log("Chess instance valid moves:", chess.moves());

            chess.reset();
            try {
                const testChess = new Chess(move.fen);
                console.log("FEN loaded successfully:", move.fen);
                chess = testChess;
                board.position(chess.fen());
            } catch (e) {
                console.error("Invalid FEN received from WebSocket:", move.fen, e);
            }

            // Log the chess instance state after making the move
            console.log("Chess instance after move:", chess.fen());
            console.log("Chess instance valid moves:", chess.moves());
        });

        $.ajax({
            url: 'http://localhost:8080/game/create',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ name: 'Host Player' }),
            success: function(response) {
                gameId = response.id;
                console.log("Game created with ID:", gameId);
            },
            error: function(xhr, status, error) {
                console.error("Error creating game:", error);
            }
        });
    }, function(error) {
        console.error('STOMP connection error: ' + error);
    });
});
