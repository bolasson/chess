package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {

    public Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard board) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(getMovesInDirection(position, new int[]{1,1}, board));
        validMoves.addAll(getMovesInDirection(position, new int[]{-1,1}, board));
        validMoves.addAll(getMovesInDirection(position, new int[]{1,-1}, board));
        validMoves.addAll(getMovesInDirection(position, new int[]{-1,-1}, board));
        return validMoves;
    }
}