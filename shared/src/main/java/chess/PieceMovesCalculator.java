package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface PieceMovesCalculator {

    Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard board);

    default boolean positionIsAvailable(ChessPosition position, ChessBoard board) {
        return board.getPiece(position) != null;
    }

    default boolean positionHasOpponent(ChessPosition position, ChessBoard board, ChessGame.TeamColor color) {
        return board.getPiece(position).getTeamColor() != color;
    }

    default Collection<ChessMove> getMovesInDirection(ChessPosition currentPosition, int[] direction, ChessBoard board) {
        return getMovesInDirection(currentPosition, direction, board,false, null);
    }

    default Collection<ChessMove> getMovesInDirection(ChessPosition currentPosition, int[] direction, ChessBoard board, boolean limited) {
        return getMovesInDirection(currentPosition, direction, board, limited, null);
    }

    default Collection<ChessMove> getMovesInDirection(ChessPosition currentPosition, int[] direction, ChessBoard board, boolean limited, ChessPiece.PieceType promotion) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int rowIndex = currentPosition.getRow();
        int colIndex = currentPosition.getColumn();
        ChessGame.TeamColor color = board.getPiece(currentPosition).getTeamColor();
        // Direction format is [+1=Up|-1=Down,+1=Right|-1=Left]
        while (true) {
            // Check if the next move is off the board
            if (direction[0] == 1 && rowIndex == 8) break;
            if (direction[0] == -1 && rowIndex == 1) break;
            if (direction[1] == 1 && rowIndex == 8) break;
            if (direction[1] == -1 && rowIndex == 1) break;
            ChessPosition endPosition = new ChessPosition(rowIndex+direction[0],colIndex+direction[1]);
            if (positionIsAvailable(endPosition, board)) {
                ChessMove move = new ChessMove(currentPosition,endPosition, promotion);
                validMoves.add(move);
            } else if (positionHasOpponent(endPosition, board, color)) {
                ChessMove move = new ChessMove(currentPosition,endPosition, promotion);
                validMoves.add(move);
                break;
            } else {
                break;
            }
        }
        return validMoves;
    }
}