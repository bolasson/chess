import chess.*;
import client.REPL;
import ui.ChessBoardRenderer;

public class Main {
    private static final boolean testboard = false;
    public static void main(String[] args) {
        if (testboard) {
            boardUITesting();
            return;
        }
        String serverURL = "http://localhost:8080";
        if (args.length > 0) {
            serverURL = args[0];
        }
        new REPL(serverURL).run();
    }

    private static void boardUITesting() {
        System.out.println("White Perspective:");
        ChessBoardRenderer.drawBoard(true);
        System.out.println("\nBlack Perspective:");
        ChessBoardRenderer.drawBoard(false);
    }
}