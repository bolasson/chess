package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessPiece[][] board;

    public ChessBoard() {
        this.board = new ChessPiece[8][8];
    }

    public static void main (String[] args) {
        ChessBoard chessBoard = new ChessBoard();
        System.out.println(chessBoard.displayBoard());
        chessBoard.resetBoard();
        System.out.println(chessBoard.displayBoard());
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessBoard that)) return false;

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

    public String displayBoard() {
        StringBuilder boardString = new StringBuilder();

        // Create the board from top to bottom (row 8 to row 1)
        for (int row = 7; row >= 0; row--) {
            boardString.append(row+1).append(" "); // Add row number

            // Create each column, wrapping each piece with '|'
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece == null) {
                    boardString.append("| "); // Empty space represented by a space between '|'
                } else {
                    boardString.append("|").append(piece.toString());
                }
            }
            boardString.append("|\n"); // Close the row with '|' and add newline after each row
        }

        // Add column labels at the bottom
        boardString.append("   a b c d e f g h\n");

        return boardString.toString();
    }

}
