package chess;

import java.util.ArrayList;
import java.util.Collection;

// All tests passed on 1/15/25

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard chessBoard;
    private TeamColor activeTeamColor;

    public ChessGame(ChessBoard board, TeamColor startTeam) {
        chessBoard = board;
        activeTeamColor = startTeam;
    }

    public ChessGame(ChessBoard board) {
        chessBoard = board;
        activeTeamColor = TeamColor.WHITE;
    }

    public ChessGame(TeamColor startTeam) {
        chessBoard = new ChessBoard();
        activeTeamColor = startTeam;
        chessBoard.resetBoard();
    }

    public ChessGame() {
        chessBoard = new ChessBoard();
        activeTeamColor = TeamColor.WHITE;
        DisplayGame.displayBoard(chessBoard.getBoard());
        chessBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return activeTeamColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        activeTeamColor = team;
    }

    public TeamColor oppositeTeamColor(TeamColor myTeamColor) {
        return myTeamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = chessBoard.getPiece(startPosition);
        if (piece == null) {
            return new ArrayList<>();
        }
        setTeamTurn(piece.getTeamColor());
        Collection<ChessMove> moves = piece.pieceMoves(chessBoard,startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        ChessBoard savedBoard = new ChessBoard(getBoard());
        for (ChessMove move : moves) {
            setBoard(new ChessBoard(savedBoard));
            getBoard().makeMove(move);
            if (!isInCheck(activeTeamColor)) {
                validMoves.add(move);
            }
        }
        setBoard(savedBoard);
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = getBoard().getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("Illegal Move: Piece does not exist");
        } else if (piece.getTeamColor() != activeTeamColor) {
            throw new InvalidMoveException("Illegal Move: Moving out of turn");
        }
        if (validMoves(move.getStartPosition()).contains(move)){
            getBoard().makeMove(move);
        } else {
            throw new InvalidMoveException("Illegal Move: Move is not legal");
        }
        if (move.getPromotionPiece() != null) {
            piece = getBoard().getPiece(move.getEndPosition());
            piece.promotePiece(move);
        }
        setTeamTurn(oppositeTeamColor(activeTeamColor));
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return chessBoard.opponentCanAttackPosition(chessBoard.getKingPosition(teamColor), oppositeTeamColor(teamColor));
    }

    public Collection<ChessMove> teamMoveSet(TeamColor teamColor) {
        Collection<ChessMove> allMoves = new ArrayList<>();
        for (var position : getBoard().getAllPositions()) {
            var piece = getBoard().getPiece(position);
            if (piece != null && piece.getTeamColor() == teamColor) {
                allMoves.addAll(validMoves(position));
            }
        }
        return allMoves;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && teamMoveSet(teamColor).isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && teamMoveSet(teamColor).isEmpty();
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }
}
