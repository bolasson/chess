package client;

import java.util.Scanner;

public class REPL {
    private final Client client;

    public REPL(String serverUrl) {
        this.client = new Client(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to the Chess Client. Type 'help' to see available commands.");
        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.print(">>> ");
            input = scanner.nextLine();
            String output = client.evaluateCommand(input);
            if (!output.isEmpty()) {
                System.out.println(output);
            }
        } while (!input.equalsIgnoreCase("quit"));
        System.out.println("Exiting Chess Client. Goodbye!");
        scanner.close();
    }
}