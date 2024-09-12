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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

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

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceMovesCalculator piece;

        // Determine which piece to use based on its type
        switch (this.type) {
//            case KING -> piece = new King();
//            case QUEEN -> piece = new Queen();
            case PieceType.BISHOP -> piece = new BishopMovesCalculator();
//            case KNIGHT -> piece = new Knight();
//            case ROOK -> piece = new Rook();
//            case PAWN -> piece = new Pawn();
            default -> throw new RuntimeException("Unknown piece type");
        }

        System.out.println(DisplayGame.displayBoard(board.getBoard()));

        // Delegate move calculation to the piece's specific move logic
        return piece.getValidMoves(myPosition, board);
    }

    @Override
    public String toString() {
        return switch (this.type) {
            case PAWN -> color == ChessGame.TeamColor.WHITE ? "P" : "p";
            case KNIGHT -> color == ChessGame.TeamColor.WHITE ? "N" : "n";
            case BISHOP -> color == ChessGame.TeamColor.WHITE ? "B" : "b";
            case ROOK -> color == ChessGame.TeamColor.WHITE ? "R" : "r";
            case QUEEN -> color == ChessGame.TeamColor.WHITE ? "Q" : "q";
            case KING -> color == ChessGame.TeamColor.WHITE ? "K" : "k";
        };
    }
}
