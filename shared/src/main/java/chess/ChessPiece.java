package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessPiece.PieceType type;
    private final ChessGame.TeamColor color;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        ChessPiece piece = (ChessPiece) o;
        return type == piece.type && color == piece.color;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + color.hashCode();
        return result;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    public void promotePiece(ChessMove move) {
        int promotionRow = color == ChessGame.TeamColor.WHITE ? 8 : 1;
        if (move.getEndPosition().getRow() == promotionRow) {
            this.type = move.getPromotionPiece();
        }
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceMovesCalculator piece;
        switch (this.type) {
            case PieceType.QUEEN -> piece = new QueenMovesCalculator();
            case PieceType.KING -> piece = new KingMovesCalculator();
            case PieceType.BISHOP -> piece = new BishopMovesCalculator();
            case PieceType.ROOK -> piece = new RookMovesCalculator();
            case PieceType.KNIGHT -> piece = new KnightMovesCalculator();
            case PieceType.PAWN -> piece = new PawnMovesCalculator();
            default -> throw new RuntimeException("Unknown piece type");
        }
        return piece.getValidMoves(myPosition, board);
    }

    @Override
    public String toString() {
        return switch (this.type) {
            case PAWN -> color == ChessGame.TeamColor.WHITE ? "♙" : "♟";
            case KNIGHT -> color == ChessGame.TeamColor.WHITE ? "♘" : "♞";
            case BISHOP -> color == ChessGame.TeamColor.WHITE ? "♗" : "♝";
            case ROOK -> color == ChessGame.TeamColor.WHITE ? "♖" : "♜";
            case QUEEN -> color == ChessGame.TeamColor.WHITE ? "♕" : "♛";
            case KING -> color == ChessGame.TeamColor.WHITE ? "♔" : "♚";
        };
    }
}