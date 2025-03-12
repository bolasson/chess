package chess.movescalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard board) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(getMovesInDirection(position, new int[]{1,0}, board));
        validMoves.addAll(getMovesInDirection(position, new int[]{-1,0}, board));
        validMoves.addAll(getMovesInDirection(position, new int[]{0,1}, board));
        validMoves.addAll(getMovesInDirection(position, new int[]{0,-1}, board));
        validMoves.addAll(getMovesInDirection(position, new int[]{1,1}, board));
        validMoves.addAll(getMovesInDirection(position, new int[]{-1,1}, board));
        validMoves.addAll(getMovesInDirection(position, new int[]{1,-1}, board));
        validMoves.addAll(getMovesInDirection(position, new int[]{-1,-1}, board));
        return validMoves;
    }
}