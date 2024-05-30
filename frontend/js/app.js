$(document).ready(function() {
    var board = Chessboard('board', {
        draggable: true,
        dropOffBoard: 'snapback',
        position: 'start',
        pieceTheme: 'img/chesspieces/wikipedia/{piece}.svg',  // Ensure this path is correct
        onDrop: function(source, target, piece, newPos, oldPos, orientation) {
            // Handle piece drop
            console.log('Piece dropped from ' + source + ' to ' + target);
            console.log('New position: ' + Chessboard.objToFen(newPos));
            // Example AJAX call to backend
            $.ajax({
                url: '/api/game/move',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    source: source,
                    target: target,
                    piece: piece,
                    newPos: Chessboard.objToFen(newPos),
                    oldPos: Chessboard.objToFen(oldPos)
                }),
                success: function(response) {
                    console.log('Move successful', response);
                },
                error: function(error) {
                    console.log('Error making move', error);
                }
            });
        }
    });

    var socket = new SockJS('/chess-websocket');
    var stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/moves', function(message) {
            var move = JSON.parse(message.body);
            board.position(move.newPos);
        });
    });
});
