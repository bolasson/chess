package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[8][8];
    }

    public ChessBoard(ChessBoard chessBoard) {
        board = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = chessBoard.board[i][j];
                if (piece != null) {
                    ChessPiece clonedPiece = new ChessPiece(piece.getTeamColor(), piece.getPieceType());
                    board[i][j] = clonedPiece;
                }
            }
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof ChessBoard that)) { return false; }

        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }


    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    public void removePiece(ChessPosition position) {
        board[position.getRow()-1][position.getColumn()-1] = null;
    }

    public void makeMove(ChessMove move){
        addPiece(move.getEndPosition(), getPiece(move.getStartPosition()));
        removePiece(move.getStartPosition());
    }

    public boolean opponentCanAttackPosition(ChessPosition targetPosition, ChessGame.TeamColor opponentColor) {
        for (ChessPosition position : getAllPositions()) {
            ChessPiece piece = getPiece(position);
            if (piece != null && piece.getTeamColor() == opponentColor) {
                Collection<ChessMove> potentialMoves = piece.pieceMoves(this, position);
                if (potentialMoves == null) { break; }
                for (ChessMove move : potentialMoves) {
                    if (move.getEndPosition().equals(targetPosition)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Collection<ChessPosition> getAllPositions() {
        Collection<ChessPosition> positions = new ArrayList<>();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                positions.add(new ChessPosition(row, col));
            }
        }
        return positions;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor teamColor) {
        ChessPosition kingPosition = null;
        for (int i=1; i < 9; i++) {
            for (int j=1; j < 9; j++) {
                ChessPosition tempPosition = new ChessPosition(i, j);
                ChessPiece piece = getPiece(tempPosition);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    kingPosition = tempPosition;
                }
            }
        }
        return kingPosition;
    }

    public ChessPiece[][] getBoard(){
        return board;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // Clear the board
        board = new ChessPiece[8][8];
        // Instantiate all the pawns
        for (int i = 1; i <= 8; i++) {
            ChessPosition whitePawnPosition = new ChessPosition(2,i);
            ChessPosition blackPawnPosition = new ChessPosition(7,i);
            addPiece(whitePawnPosition, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(blackPawnPosition, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
        // Instantiate special pieces for each team
        instantiateSpecialPieces(ChessGame.TeamColor.WHITE);
        instantiateSpecialPieces(ChessGame.TeamColor.BLACK);
    }

    private void instantiateSpecialPieces(ChessGame.TeamColor color) {
        // List of the unique pieces to instantiate, from right to left
        ChessPiece.PieceType[] pieces = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING
        };
        // Determine which row to instantiate on based on team color
        int row = color == ChessGame.TeamColor.WHITE ? 1 : 8;
        // Iterate through the pieces to add
        for (int i = 1; i <= pieces.length; i++) {
            // Instantiate the corresponding piece in the list pieces at the specified position
            ChessPosition position = new ChessPosition(row,i);
            addPiece(position, new ChessPiece(color, pieces[i - 1]));
            // Instantiate the matching pieces on the opposite side of the board, if applicable
            if (i <= 3) {
                ChessPosition mirrorPosition = new ChessPosition(row,9-i);
                addPiece(mirrorPosition, new ChessPiece(color, pieces[i - 1]));
            }
        }
    }
}