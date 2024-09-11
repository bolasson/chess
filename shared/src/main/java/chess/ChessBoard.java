package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessPiece[][] board = new ChessPiece[8][8];

    public ChessBoard() {
        
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
        // Instantiate all the pawns
        for (int i = 1; i <= 8; i++) {
            ChessPosition whitePawnPosition = new ChessPosition(2,i);
            ChessPosition blackPawnPosition = new ChessPosition(7,i);
            addPiece(whitePawnPosition, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(blackPawnPosition, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }

        // Instantiate white piece positions
        ChessPosition whiteRookLeft = new ChessPosition(1,1);
        ChessPosition whiteKnightLeft = new ChessPosition(1,2);
        ChessPosition whiteBishopLeft = new ChessPosition(1,3);
        ChessPosition whiteQueen = new ChessPosition(1,4);
        ChessPosition whiteKing = new ChessPosition(1,5);
        ChessPosition whiteBishopRight = new ChessPosition(1,6);
        ChessPosition whiteKnightRight = new ChessPosition(1,7);
        ChessPosition whiteRookRight = new ChessPosition(1,8);
        // Instantiate white pieces
        addPiece(whiteRookLeft, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(whiteKnightLeft, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(whiteBishopLeft, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(whiteQueen, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(whiteKing, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(whiteBishopRight, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(whiteKnightRight, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(whiteRookRight, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        // Instantiate black piece positions
        ChessPosition blackRookLeft = new ChessPosition(8,1);
        ChessPosition blackKnightLeft = new ChessPosition(8,2);
        ChessPosition blackBishopLeft = new ChessPosition(8,3);
        ChessPosition blackQueen = new ChessPosition(8,4);
        ChessPosition blackKing = new ChessPosition(8,5);
        ChessPosition blackBishopRight = new ChessPosition(8,6);
        ChessPosition blackKnightRight = new ChessPosition(8,7);
        ChessPosition blackRookRight = new ChessPosition(8,8);
        // Instantiate black pieces
        addPiece(blackRookLeft, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(blackKnightLeft, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(blackBishopLeft, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(blackQueen, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(blackKing, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(blackBishopRight, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(blackKnightRight, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(blackRookRight, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
    }

    // Helper function for resetting the board
    private void InstantiateSpecialPieces(ChessGame.TeamColor color) {
        ChessPiece.PieceType[] pieces = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING
        };

        for (int i = 1; i <= pieces.length; i++) {
            // Instantiate the corresponding piece in the list pieces at the specified position
            ChessPosition position = new ChessPosition(1,i);
            addPiece(position, new ChessPiece(color, pieces[i - 1]));
            // Instantiate the matching pieces on the opposite side of the board
            if (i <= 3) {
                ChessPosition mirrorPosition = new ChessPosition(1,9-i);
                addPiece(mirrorPosition, new ChessPiece(color, pieces[i - 1]));
            }
        }
    }
}
