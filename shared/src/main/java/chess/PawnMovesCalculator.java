package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {

    private Collection<ChessMove> validMoves = new ArrayList<>();
    private ChessPosition startPosition;
    private ChessBoard board;
    private ChessPosition targetPosition;
    private ChessGame.TeamColor color;

    public Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard selectedBoard) {
        board = selectedBoard;
        startPosition = position;
        color = board.getPiece(startPosition).getTeamColor();
        int direction = color == ChessGame.TeamColor.WHITE ? 1 : -1;
        // Move the pawn one forward if possible
        ChessPosition oneForward = new ChessPosition(startPosition.getRow() + direction, startPosition.getColumn());
        targetPosition = oneForward;
        if (isWithinBounds() && positionIsAvailable(targetPosition, board)) {
            addPawnMoves();
        }
        // Move the pawn two forward if the pawn is on the starting row
        targetPosition = new ChessPosition(startPosition.getRow() + direction*2, startPosition.getColumn());
        if (isOnStartRow() && positionIsAvailable(targetPosition, board) && positionIsAvailable(oneForward, board)) {
            addPawnMoves();
        }

        // Check the left attack position
        targetPosition = new ChessPosition(startPosition.getRow() + direction, startPosition.getColumn() - 1);
        if (isWithinBounds() && !positionIsAvailable(targetPosition, board) && positionHasOpponent(targetPosition, board, color)) {
            addPawnMoves();
        }
        // Check the right attack position
        targetPosition = new ChessPosition(startPosition.getRow() + direction, startPosition.getColumn() + 1);
        if (isWithinBounds() && !positionIsAvailable(targetPosition, board) && positionHasOpponent(targetPosition, board, color)) {
            addPawnMoves();
        }
        return validMoves;
    }

    public void addPawnMoves() {
        if (targetPosition.getRow() == 8 || targetPosition.getRow() == 1) {
            validMoves.add(new ChessMove(startPosition, targetPosition, ChessPiece.PieceType.QUEEN));
            validMoves.add(new ChessMove(startPosition, targetPosition, ChessPiece.PieceType.KNIGHT));
            validMoves.add(new ChessMove(startPosition, targetPosition, ChessPiece.PieceType.ROOK));
            validMoves.add(new ChessMove(startPosition, targetPosition, ChessPiece.PieceType.BISHOP));
        } else {
            validMoves.add(new ChessMove(startPosition, targetPosition, null));
        }
    }

    private boolean isWithinBounds() {
        // Will the next move be within the vertical bounds of the board
        if (targetPosition.getRow() > 8 || targetPosition.getRow() < 1) { return false; }
        // Will the next move be within the horizontal bounds of the board
        return !(targetPosition.getColumn() > 8 || targetPosition.getColumn() < 1);
    }

    private boolean isOnStartRow() {
        if (color == ChessGame.TeamColor.WHITE && startPosition.getRow() == 2) { return true; }
        return (color == ChessGame.TeamColor.BLACK && startPosition.getRow() == 7);
    }
}