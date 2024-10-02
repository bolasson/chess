package chess;

public class DisplayGame {
    public static String displayBoard(ChessPiece[][] board) {
        StringBuilder boardString = new StringBuilder();
        // Create the board from top to bottom (row 8 to row 1)
        for (int row = 7; row >= 0; row--) {
            boardString.append(row+1).append(" "); // Add row number
            // Create each column, wrapping each piece with '|'
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece == null) {
                    boardString.append("|   "); // Empty space represented by a space between '|'
                } else {
                    boardString.append("|").append(piece.toString());
                }
            }
            boardString.append("|\n"); // Close the row with '|' and add newline after each row
        }
        // Add column labels at the bottom
        boardString.append("    A   B   C   D   E   F   G   H\n");
        return boardString.toString();
    }
}