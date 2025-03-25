package client;

import model.GameData;
import results.*;
import server.ServerFacade;
import server.ResponseException;
import ui.ChessBoardRenderer;
import ui.EscapeSequences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {

    public enum State {
        LOGGED_OUT,
        LOGGED_IN
    }

    public State currentState;
    public String username;
    private final ServerFacade server;
    private String authToken = "";
    private List<GameData> availableGames = new ArrayList<>();

    public Client(String serverURL) {
        this.server = new ServerFacade(serverURL);
        this.currentState = State.LOGGED_OUT;
    }

    public String evaluateCommand(String input, java.util.Scanner scanner) {
        String command = input.trim().toLowerCase();
        if (currentState == State.LOGGED_OUT) {
            return switch (command) {
                case "help", "h" -> getHelpText();
                case "quit", "q", "exit", "e" -> "";
                case "login", "l" -> login(scanner);
                case "register", "r" -> register(scanner);
                default -> "Unknown command.\n\nType '" + Keyword("h") + "elp' for available commands.";
            };
        } else {
            return switch (command) {
                case "help", "h" -> getHelpText();
                case "logout", "lo" -> logout();
                case "quit", "q", "exit", "e" -> {
                    logout();
                    yield "";
                }
                case "create game", "create", "c" -> createGame(scanner);
                case "list games", "list", "l" -> listGames();
                case "play game", "play", "join game", "join", "p", "j" -> playGame(scanner);
                case "observe game", "observe", "o" -> observeGame(scanner);
                default -> "Unknown command.\n\nType '" + Keyword("h") + "elp' for available commands.";
            };
        }
    }

    private String getHelpText() {
        if (currentState == State.LOGGED_OUT) {
            return "Login Page:\n" +
                    "- " + Keyword("l") + "ogin: Login to your account\n" +
                    "- " + Keyword("r") + "egister: Register a new account\n" +
                    "- " + Keyword("h") + "elp: Display this help text\n" +
                    "- " + Keyword("q") + "uit: Exit the application\n";
        } else {
            return "Main Menu:\n" +
                    "- " + Keyword("c") + "reate game: Create a new game\n" +
                    "- " + Keyword("l") + "ist games: List available games\n" +
                    "- " + Keyword("p") + "lay game: Join a game as a player\n" +
                    "- " + Keyword("o") + "bserve game: Join a game as an observer\n" +
                    "- " + Keyword("lo") + "gout: Logout and return to the login page\n" +
                    "- " + Keyword("h") + "elp: Display this help text\n" +
                    "- " + Keyword("q") + "uit: Exit the application\n";

        }
    }

// Login Page Options
    private String login(java.util.Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            LoginResult response = server.login(username, password);
            currentState = State.LOGGED_IN;
            authToken = response.authToken();
            UpdateGameList();
            this.username = response.username();
            return "Signed in as " + this.username + ".\n\nType '" + Keyword("h") + "elp' to see available commands.";
        } catch (ResponseException ex) {
            return ErrorMessage("Login failed: " + ex.getMessage());
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
            currentState = State.LOGGED_IN;
            authToken = response.authToken();
            UpdateGameList();
            this.username = response.username();
            return "Created and signed in as " + username + ".\n\nType '" + Keyword("h") + "elp' to see available commands.";
        } catch (ResponseException ex) {
            return ErrorMessage("Registration failed: " + ex.getMessage());
        }
    }

// Main Menu Options
    private String logout() {
        try {
            currentState = State.LOGGED_OUT;
            server.logout(authToken);
            authToken = "";
            return "Logged out successfully.\n\nType '" + Keyword("h") + "elp' to see available commands.";
        } catch (ResponseException ex) {
            return ErrorMessage("Logout failed: " + ex.getMessage());
        }
    }

    private String createGame(java.util.Scanner scanner) {
        System.out.print("Enter game name: ");
        String gameName = scanner.nextLine();
        try {
            CreateGameResult createGameResult = server.createGame(gameName, authToken);
            UpdateGameList();
            int createGameIndex = 0;
            for (int i = 0; i < availableGames.size(); i++) {
                if (availableGames.get(i).gameID() == createGameResult.gameID()) {
                    createGameIndex = i+1;
                    break;
                }
            }
            return "Game created with name " + createGameResult.gameName() + EscapeSequences.SET_TEXT_ITALIC + ".\nTo join this game, enter the command '" + Keyword("p") + "lay game', then enter " + createGameIndex + EscapeSequences.RESET_TEXT_ITALIC;
        } catch (ResponseException ex) {
            return ErrorMessage("Failed to create game: " + ex.getMessage());
        }
    }

    private String listGames() {
        UpdateGameList();
        for (int i = 0; i < availableGames.size(); i++) {
            GameData game = availableGames.get(i);
            System.out.println((i+1) + game.toString());
        }
        return EscapeSequences.SET_TEXT_ITALIC + "\nTo join a game, enter '" + Keyword("p") + "lay game' then provide the game ID from the list above." + EscapeSequences.RESET_TEXT_ITALIC;
    }

    private void UpdateGameList() {
        try {
            ListGamesResult result = server.listGames(authToken);
            availableGames.clear();
            availableGames = result.games();
        } catch (ResponseException ex) {
            System.out.println(ErrorMessage("Failed to pull games from the database: " + ex.getMessage()));
        }
    }

    private String playGame(java.util.Scanner scanner) {
        MiniREPL gameNumberREPL = new MiniREPL("Enter game ID: ",
                input -> {
                    int proposedGameID;
                    try {
                        proposedGameID = Integer.parseInt(input);
                    } catch (Exception e) {
                        throw new Exception("Input must be an integer.");
                    }
                    if (availableGames.size() + 1 < proposedGameID) {
                        throw new Exception("Game ID is not in game list. To view the game list enter '" + Keyword("e") + SetErrorFormating() + "xit', and then '" + Keyword("l") + SetErrorFormating() + "ist games'.");
                    }
                    return "valid";
                });
        String gameNumberResponse = gameNumberREPL.run(scanner);
        GameData selectedGame;
        if (gameNumberResponse.equalsIgnoreCase("quit")) {
            return "The user quit the operation early.";
        } else {
            selectedGame = availableGames.get(Integer.parseInt(gameNumberResponse)-1);
        }

        MiniREPL colorREPL = new MiniREPL("Enter desired color (white/black): ",
                input -> {
                    if (!MiniREPL.listContainsValue(Arrays.asList("white", "w", "black", "b"),input)) {
                        throw new Exception("Invalid color. Please enter '" + Keyword("w") + SetErrorFormating() + "hite' or '" + Keyword("b") + SetErrorFormating() + "lack'.");
                    }
                    return "valid";
                });
        String color = colorREPL.run(scanner);
        if (color.equals("w")) color = "white";
        if (color.equals("b")) color = "black";
        try {
            server.joinGame(selectedGame.gameID(), color, authToken);
            ChessBoardRenderer.drawBoard(color.equals("white"));
            return "Joined the game '" + selectedGame.gameName() + "' as the " + color + " player.";
        } catch (ResponseException ex) {
            return ErrorMessage("Failed to join game: " + ex.getMessage());
        }
    }

    private String observeGame(java.util.Scanner scanner) {
        MiniREPL gameNumberREPL = new MiniREPL("Enter the ID of the game you want to observe: ",
                input -> {
                    int proposedGameID;
                    try {
                        proposedGameID = Integer.parseInt(input);
                    } catch (Exception e) {
                        throw new Exception("Input must be an integer.");
                    }
                    if (availableGames.size() + 1 < proposedGameID) {
                        throw new Exception("Game ID is not in game list. To view the game list enter '" + Keyword("e") + SetErrorFormating() + "xit', and then '" + Keyword("l") + SetErrorFormating() + "ist games'.");
                    }
                    return "valid";
                });
        String gameNumberResponse = gameNumberREPL.run(scanner);
        GameData selectedGame;
        if (gameNumberResponse.equalsIgnoreCase("quit")) {
            return "The user quit the operation early.";
        } else {
            selectedGame = availableGames.get(Integer.parseInt(gameNumberResponse)-1);
        }
        try {
            ChessBoardRenderer.drawBoard(true);
            return server.observeGame(selectedGame.gameID(), selectedGame.gameName(), authToken);
        } catch (ResponseException ex) {
            return ErrorMessage("Failed to observe game: " + ex.getMessage());
        }
    }

    public static String ErrorMessage(String message) {
        message = message.replace("Error: ", "");
        return SetErrorFormating() + message + EscapeSequences.RESET_TEXT_ITALIC + EscapeSequences.RESET_TEXT_COLOR;
    }

    public static String SetErrorFormating() {
        return EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.SET_TEXT_ITALIC;
    }

    public static String Keyword(String message) {
        return EscapeSequences.SET_TEXT_UNDERLINE + EscapeSequences.SET_TEXT_COLOR_YELLOW + message + EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_TEXT_UNDERLINE;
    }
}