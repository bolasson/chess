package client;

import model.GameData;
import results.*;
import server.ServerFacade;
import server.ResponseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {

    private enum State {
        PRELOGIN,
        POSTLOGIN
    }

    private State currentState;
    private final ServerFacade server;
    private String authToken = "";
    private final List<String> quitStrings = Arrays.asList("quit", "exit", "stop", "close", "q", "e", "s","c");
    private List<GameData> availableGames = new ArrayList<GameData>();

    public Client(String serverURL) {
        this.server = new ServerFacade(serverURL);
        this.currentState = State.PRELOGIN;
    }

    public String evaluateCommand(String input, java.util.Scanner scanner) {
        String command = input.trim().toLowerCase();
        if (currentState == State.PRELOGIN) {
            return switch (command) {
                case "help", "h" -> getHelpText();
                case "quit", "q", "exit", "e" -> "";
                case "login", "l" -> login(scanner);
                case "register", "r" -> register(scanner);
                default -> "Unknown command. Type 'help' for available commands.";
            };
        } else {
            return switch (command) {
                case "help", "h" -> getHelpText();
                case "logout" -> logout();
                case "quit", "q", "exit", "e" -> {
                    logout();
                    yield "";
                }
                case "create game", "create", "c" -> createGame(scanner);
                case "list games", "list", "l" -> listGames();
                case "play game", "play", "join game", "join", "p", "j" -> playGame(scanner);
                case "observe game", "observe", "o" -> observeGame(scanner);
                default -> "Unknown command. Type 'help' for available commands.";
            };
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
            CreateGameResult createGameResult = server.createGame(gameName, authToken);
            ListGamesResult listGamesResult = server.listGames(authToken);
            UpdateGameList(listGamesResult.games());
            int createGameIndex = 0;
            for (int i = 0; i < availableGames.size(); i++) {
                if (availableGames.get(i).gameID() == createGameResult.gameID()) {
                    createGameIndex = i+1;
                    break;
                }
            }
            return "Game created with name " + createGameResult.gameName() + ".\nTo join this game, enter the command 'play game', then enter " + createGameIndex;
        } catch (ResponseException ex) {
            return "Failed to create game: " + ex.getMessage();
        }
    }

    private String listGames() {
        try {
            ListGamesResult result = server.listGames(authToken);
            UpdateGameList(result.games());
            for (int i = 0; i < availableGames.size(); i++) {
                GameData game = availableGames.get(i);
                System.out.println((i+1) + game.toString());
            }
            return "\nTo join a game, enter 'play game' then provide the game number from the list above.";
        } catch (ResponseException ex) {
            return "Failed to list games: " + ex.getMessage();
        }
    }

    private void UpdateGameList(List<GameData> games) {
        availableGames.clear();
        availableGames = games;
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
        try {
            return server.joinGame(gameNumber, color, authToken).message();
        } catch (ResponseException ex) {
            return "Failed to join game: " + ex.getMessage();
        }
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
        try {
            return server.observeGame(gameNumber, authToken);
        } catch (ResponseException ex) {
            return "Failed to observe game: " + ex.getMessage();
        }
    }
}