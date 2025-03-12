package chess.movescalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard board) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(getMovesInDirection(position, new int[]{2,-1}, board, true));
        validMoves.addAll(getMovesInDirection(position, new int[]{2,1}, board,true));
        validMoves.addAll(getMovesInDirection(position, new int[]{-2,-1}, board,true));
        validMoves.addAll(getMovesInDirection(position, new int[]{-2,1}, board,true));
        validMoves.addAll(getMovesInDirection(position, new int[]{1,2}, board,true));
        validMoves.addAll(getMovesInDirection(position, new int[]{-1,2}, board,true));
        validMoves.addAll(getMovesInDirection(position, new int[]{1,-2}, board,true));
        validMoves.addAll(getMovesInDirection(position, new int[]{-1,-2}, board,true));
        return validMoves;
    }
}