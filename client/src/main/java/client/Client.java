package client;

import results.LoginResult;
import results.LogoutResult;
import results.RegisterResult;
import server.ServerFacade;
import server.ResponseException;
import model.AuthData;

import java.util.Arrays;
import java.util.List;

public class Client {

    private enum State {
        PRELOGIN,
        POSTLOGIN
    }

    private State currentState;
    private final String serverURL;
    private ServerFacade server;
    private String authToken = "";
    private static final List<String> quitStrings = Arrays.asList("quit", "exit", "stop", "close", "q", "e", "s","c");

    public Client(String serverURL) {
        this.server = new ServerFacade(serverURL);
        this.serverURL = serverURL;
        this.currentState = State.PRELOGIN;
    }

    public String evaluateCommand(String input, java.util.Scanner scanner) {
        String command = input.trim().toLowerCase();
        if (currentState == State.PRELOGIN) {
            switch (command) {
                case "h":
                case "-h":
                case "help":
                    return getHelpText();
                case "q":
                case "quit":
                case "exit":
                    return "";
                case "login":
                    return login(scanner);
                case "register":
                    return register(scanner);
                default:
                    return "Unknown command. Type 'help' for available commands.";
            }
        } else {
            switch (command) {
                case "h":
                case "-h":
                case "help":
                    return getHelpText();
                case "logout":
                    return logout();
                case "q":
                case "quit":
                case "exit":
                    logout();
                    return "";
                case "create game":
                    return createGame(scanner);
                case "list games":
                    return listGames();
                case "play game":
                    return playGame(scanner);
                case "observe game":
                    return observeGame(scanner);
                default:
                    return "Unknown command. Type 'help' for available commands.";
            }
        }
    }

    private String getHelpText() {
        if (currentState == State.PRELOGIN) {
            return "Prelogin Commands:\n" +
                    "- help: Display this help text\n" +
                    "- quit: Exit the application\n" +
                    "- login: Login to your account\n" +
                    "- register: Register a new account";
        } else {
            return "Postlogin Commands:\n" +
                    "- help: Display this help text\n" +
                    "- logout: Logout and return to Prelogin UI\n" +
                    "- create game: Create a new game\n" +
                    "- list games: List available games\n" +
                    "- play game: Join a game as a player\n" +
                    "- observe game: Join a game as an observer";
        }
    }

// Prelogin Options
    private String login(java.util.Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            LoginResult response = server.login(username, password);
            currentState = State.POSTLOGIN;
            authToken = response.authToken();
            return "Signed in as " + response.username() + ".\nType 'help' to see available commands.";
        } catch (ResponseException ex) {
            return "Login failed: " + ex.getMessage();
        }
    }

    private String register(java.util.Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        try {
            RegisterResult response = server.register(username, password, email);
            currentState = State.POSTLOGIN;
            authToken = response.authToken();
            return "Created and signed in as " + response.username() + ".\nType 'help' to see available commands.";
        } catch (ResponseException ex) {
            return "Registration failed: " + ex.getMessage();
        }
    }

// Postlogin Options
    private String logout() {
        try {
            currentState = State.PRELOGIN;
            LogoutResult response = server.logout(authToken);
            authToken = "";
            return response.message() + ".\nType 'help' to see available commands.";
        } catch (ResponseException ex) {
            return "Logout failed: " + ex.getMessage();
        }
    }

    private String createGame(java.util.Scanner scanner) {
        System.out.print("Enter game name: ");
        String gameName = scanner.nextLine();
        try {
            return "Game created with name " + server.createGame(gameName, authToken).gameName();
        } catch (ResponseException ex) {
            return "Failed to create game: " + ex.getMessage();
        }
    }

    private String listGames() {
        return server.listGames(authToken);
    }

    private String playGame(java.util.Scanner scanner) {
        System.out.print("Enter game number: ");
        String gameNumberStr = scanner.nextLine();
        int gameNumber;
        while (true) {
            try {
                String finalGameNumberStr = gameNumberStr;
                if (quitStrings.stream().anyMatch(s -> s.equalsIgnoreCase(finalGameNumberStr))) {
                    return "Leaving play game operation.";
                }

                gameNumber = Integer.parseInt(gameNumberStr);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid game number. Please enter a valid integer.");
                gameNumberStr = scanner.nextLine();
            }
        }
        System.out.print("Enter desired color (white/black): ");
        String color = scanner.nextLine().toLowerCase();
        if (!color.equals("white") && !color.equals("w") && !color.equals("black") && !color.equals("b")) {
            return "Invalid color. Please enter 'white' or 'black'.";
        }
        if (color.equals("w")) color = "white";
        if (color.equals("b")) color = "black";
        return server.joinGame(gameNumber, color, authToken);
    }

    private String observeGame(java.util.Scanner scanner) {
        System.out.print("Enter game number to observe: ");
        String gameNumberStr = scanner.nextLine();
        int gameNumber;
        while (true) {
            try {
                String finalGameNumberStr = gameNumberStr;
                if (quitStrings.stream().anyMatch(s -> s.equalsIgnoreCase(finalGameNumberStr))) {
                    return "Leaving observe game operation.";
                }

                gameNumber = Integer.parseInt(gameNumberStr);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid game number. Please enter a valid integer.");
                gameNumberStr = scanner.nextLine();
            }
        }
        return server.observeGame(gameNumber, authToken);
    }
}