package client;

import ui.EscapeSequences;

import java.util.Scanner;

public class REPL {
    private final Client client;

    public REPL(String serverURL) {
        this.client = new Client(serverURL);
    }

    public void run() {
        System.out.println("Welcome to my chess application. Type '" + Client.keyword("h") + "elp' to see available commands.");
        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            String userText = EscapeSequences.SET_TEXT_COLOR_RED + client.currentState;
            if (client.currentState == Client.State.LOGGED_IN) {
                userText = EscapeSequences.SET_TEXT_COLOR_GREEN + client.username;
            }
            System.out.print("[" + userText + EscapeSequences.RESET_TEXT_COLOR + "] >>> ");
            input = scanner.nextLine();
            String output = client.evaluateCommand(input, scanner);
            if (output == null || !output.isEmpty()) {
                System.out.println(output);
            } else {
                break;
            }
        } while (!input.equalsIgnoreCase("quit"));
        System.out.println("Exiting application. Goodbye!\n");
        scanner.close();
    }
}