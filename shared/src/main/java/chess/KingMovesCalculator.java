package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard board) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(getMovesInDirection(position, new int[]{1,0}, board, true));
        validMoves.addAll(getMovesInDirection(position, new int[]{-1,0}, board,true));
        validMoves.addAll(getMovesInDirection(position, new int[]{0,1}, board,true));
        validMoves.addAll(getMovesInDirection(position, new int[]{0,-1}, board,true));
        validMoves.addAll(getMovesInDirection(position, new int[]{1,1}, board,true));
        validMoves.addAll(getMovesInDirection(position, new int[]{-1,1}, board,true));
        validMoves.addAll(getMovesInDirection(position, new int[]{1,-1}, board,true));
        validMoves.addAll(getMovesInDirection(position, new int[]{-1,-1}, board,true));
        return validMoves;
    }
}