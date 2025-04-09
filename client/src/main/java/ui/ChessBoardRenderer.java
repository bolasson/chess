package ui;

public class ChessBoardRenderer {

    public static void drawBoard(boolean whitePerspective) {
        System.out.println();
        String[][] board = new String[8][8];

        board[0][0] = EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.WHITE_ROOK;
        board[0][1] = EscapeSequences.WHITE_KNIGHT;
        board[0][2] = EscapeSequences.WHITE_BISHOP;
        board[0][3] = EscapeSequences.WHITE_QUEEN;
        board[0][4] = EscapeSequences.WHITE_KING;
        board[0][5] = EscapeSequences.WHITE_BISHOP;
        board[0][6] = EscapeSequences.WHITE_KNIGHT;
        board[0][7] = EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.WHITE_ROOK;
        for (int i = 0; i < 8; i++) {
            board[1][i] = EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.WHITE_PAWN;
        }

        for (int r = 2; r <= 5; r++) {
            for (int c = 0; c < 8; c++) {
                board[r][c] = EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.EMPTY;
            }
        }

        for (int i = 0; i < 8; i++) {
            board[6][i] = EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.BLACK_PAWN;
        }
        board[7][0] = EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.BLACK_ROOK;
        board[7][1] = EscapeSequences.BLACK_KNIGHT;
        board[7][2] = EscapeSequences.BLACK_BISHOP;
        board[7][3] = EscapeSequences.BLACK_QUEEN;
        board[7][4] = EscapeSequences.BLACK_KING;
        board[7][5] = EscapeSequences.BLACK_BISHOP;
        board[7][6] = EscapeSequences.BLACK_KNIGHT;
        board[7][7] = EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.BLACK_ROOK;

        int[] rowOrder = new int[8];
        int[] colOrder = new int[8];
        if (whitePerspective) {
            for (int i = 0; i < 8; i++) {
                rowOrder[i] = 7 - i;
                colOrder[i] = i;
            }
        } else {
            for (int i = 0; i < 8; i++) {
                rowOrder[i] = i;
                colOrder[i] = 7 - i;
            }
        }

        String[] labels = {"A", "B", "C", "D", "E", "F", "G", "H"};

        for (int r = 0; r < 8; r++) {
            int boardRow = rowOrder[r];
            System.out.print(" " + EscapeSequences.RESET_TEXT_COLOR + (boardRow + 1) + " ");
            for (int c = 0; c < 8; c++) {
                int boardCol = colOrder[c];
                String bgColor = ((boardRow + boardCol) % 2 == 0)
                        ? EscapeSequences.SET_BG_COLOR_LIGHT_GREEN
                        : EscapeSequences.SET_BG_COLOR_LIGHT_YELLOW;
                System.out.print(bgColor + board[boardRow][boardCol] + EscapeSequences.RESET_BG_COLOR);
            }
            System.out.print(" " + EscapeSequences.RESET_TEXT_COLOR + (boardRow + 1) + " ");
            System.out.println();
        }

         printColumnLabels(labels, whitePerspective);
    }

    private static void printColumnLabels(String[] labels, boolean whitePerspective) {
        if (!whitePerspective) {
            String[] reversed = new String[8];
            for (int i = 0; i < 8; i++) {
                reversed[i] = EscapeSequences.RESET_TEXT_COLOR + labels[7 - i];
            }
            labels = reversed;
        }
        System.out.print("   ");
        for (int i = 0; i < 8; i++) {
            System.out.print(EscapeSequences.RESET_TEXT_COLOR + "  " + labels[i] + "  ");
        }
        System.out.println();
    }
}