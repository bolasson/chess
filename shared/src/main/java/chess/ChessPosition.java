package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }

    @Override
    public String toString() {
        String returnValue = "";
        switch (col) {
            case 1 -> returnValue = "A";
            case 2 -> returnValue = "B";
            case 3 -> returnValue = "C";
            case 4 -> returnValue = "D";
            case 5 -> returnValue = "E";
            case 6 -> returnValue = "F";
            case 7 -> returnValue = "G";
            case 8 -> returnValue = "H";
        };
        returnValue += String.valueOf(row);
        return returnValue;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }
}