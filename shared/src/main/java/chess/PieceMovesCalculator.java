package chess;

import java.util.ArrayList;
import java.util.Collection;

public interface PieceMovesCalculator {

    Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard board);

    default boolean positionIsAvailable(ChessPosition position, ChessBoard board) {
        return board.getPiece(position) == null;
    }

    default boolean positionHasOpponent(ChessPosition position, ChessBoard board, ChessGame.TeamColor color) {
        return board.getPiece(position).getTeamColor() != color;
    }

    default Collection<ChessMove> getMovesInDirection(ChessPosition currentPosition, int[] direction, ChessBoard board) {
        return getMovesInDirection(currentPosition, direction, board,false);
    }

    default Collection<ChessMove> getMovesInDirection(ChessPosition currentPosition, int[] direction, ChessBoard board, boolean limited) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int rowIndex = currentPosition.getRow();
        int colIndex = currentPosition.getColumn();
        ChessGame.TeamColor color = board.getPiece(currentPosition).getTeamColor();
        // Direction format is [+1=Up|-1=Down,+1=Right|-1=Left]
        while (isWithinBounds(rowIndex, colIndex, direction)) {
            rowIndex += direction[0];
            colIndex += direction[1];
            ChessPosition endPosition = new ChessPosition(rowIndex,colIndex);
            if (positionIsAvailable(endPosition, board)) {
                ChessMove move = new ChessMove(currentPosition,endPosition, null);
                validMoves.add(move);
            } else if (positionHasOpponent(endPosition, board, color)) {
                ChessMove move = new ChessMove(currentPosition, endPosition, null);
                validMoves.add(move);
                break;
            } else {
                break;
            }
            if (limited) { break; }
        }
        return validMoves;
    }

    private boolean isWithinBounds(int row, int col, int[] direction) {
        // Will the next move be within the vertical bounds of the board
        if (direction[0] + row > 8 || direction[0] + row < 1) { return false; }
        // Will the next move be within the horizontal bounds of the board
        return !(direction[1] + col > 8 || direction[1] + col < 1);
    }
}