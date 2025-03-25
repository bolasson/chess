package client;

import java.util.Scanner;

public class REPL {
    private final Client client;

    public REPL(String serverURL) {
        this.client = new Client(serverURL);
    }

    public void run() {
        System.out.println("Welcome to my chess application. Type 'help' to see available commands.");
        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.print(">>> ");
            input = scanner.nextLine();
            String output = client.evaluateCommand(input, scanner);
            if (output == null || !output.isEmpty()) {
                System.out.println(output);
            } else {
                break;
            }
        } while (!input.equalsIgnoreCase("quit"));
        System.out.println("Exiting application. Goodbye!");
        scanner.close();
    }
}