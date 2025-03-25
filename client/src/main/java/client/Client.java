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
        PRE_LOGIN,
        POST_LOGIN
    }

    private State currentState;
    private final ServerFacade server;
    private String authToken = "";
    private final List<String> quitStrings = Arrays.asList("quit", "exit", "stop", "close", "q", "e", "s","c");
    private List<GameData> availableGames = new ArrayList<GameData>();

    public Client(String serverURL) {
        this.server = new ServerFacade(serverURL);
        this.currentState = State.PRE_LOGIN;
    }

    public String evaluateCommand(String input, java.util.Scanner scanner) {
        String command = input.trim().toLowerCase();
        if (currentState == State.PRE_LOGIN) {
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
        if (currentState == State.PRE_LOGIN) {
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
            currentState = State.POST_LOGIN;
            authToken = response.authToken();
            UpdateGameList();
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
            currentState = State.POST_LOGIN;
            authToken = response.authToken();
            UpdateGameList();
            return "Created and signed in as " + response.username() + ".\nType 'help' to see available commands.";
        } catch (ResponseException ex) {
            return "Registration failed: " + ex.getMessage();
        }
    }

// Postlogin Options
    private String logout() {
        try {
            currentState = State.PRE_LOGIN;
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
            UpdateGameList();
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
        UpdateGameList();
        for (int i = 0; i < availableGames.size(); i++) {
            GameData game = availableGames.get(i);
            System.out.println((i+1) + game.toString());
        }
        return "\nTo join a game, enter 'play game' then provide the game number from the list above.";
    }

    private void UpdateGameList() {
        try {
            ListGamesResult result = server.listGames(authToken);
            availableGames.clear();
            availableGames = result.games();
        } catch (ResponseException ex) {
            System.out.println("Failed to pull games from the database: " + ex.getMessage());
        }
    }

    private String playGame(java.util.Scanner scanner) {
        MiniREPL gameNumberREPL = new MiniREPL("Enter game number: ",
                new ValidateInput() {
                    @Override
                    public String isValid(String input) throws Exception {
                        int proposedGameID = 0;
                        try {
                            proposedGameID = Integer.parseInt(input);
                        } catch (Exception e) {
                            throw new Exception("Input must be an integer.");
                        }
                        if (availableGames.size() + 1 < proposedGameID) {
                            throw new Exception("Game ID is not in game list. To view the game list enter 'exit', and then 'list games'.");
                        }
                        return "valid";
                    }
                });
        String gameNumberResponse = gameNumberREPL.run(scanner);
        GameData selectedGame = null;
        if (gameNumberResponse.equalsIgnoreCase("quit")) {
            return "The user quit the operation early.";
        } else {
            selectedGame = availableGames.get(Integer.parseInt(gameNumberResponse)-1);
        }

        MiniREPL colorREPL = new MiniREPL("Enter desired color (white/black): ",
                new ValidateInput() {
                    @Override
                    public String isValid(String input) throws Exception {
                        if (!MiniREPL.listContainsValue(Arrays.asList("white", "w", "black", "b"),input)) {
                            throw new Exception("Invalid color. Please enter 'white' or 'black'.");
                        }
                        return "valid";
                    }
                });
        String color = colorREPL.run(scanner);
        if (color.equals("w")) color = "white";
        if (color.equals("b")) color = "black";
        try {
            JoinGameResult result = server.joinGame(selectedGame.gameID(), color, authToken);
            return "Joined the game '" + selectedGame.gameName() + "' as the " + color + " player.";
        } catch (ResponseException ex) {
            return "Failed to join game: " + ex.getMessage();
        }
    }

    private String observeGame(java.util.Scanner scanner) {
        MiniREPL gameNumberREPL = new MiniREPL("Enter the number of the game you want to observe: ",
                new ValidateInput() {
                    @Override
                    public String isValid(String input) throws Exception {
                        int proposedGameID = 0;
                        try {
                            proposedGameID = Integer.parseInt(input);
                        } catch (Exception e) {
                            throw new Exception("Input must be an integer.");
                        }
                        if (availableGames.size() + 1 < proposedGameID) {
                            throw new Exception("Game ID is not in game list. To view the game list enter 'exit', and then 'list games'.");
                        }
                        return "valid";
                    }
                });
        String gameNumberResponse = gameNumberREPL.run(scanner);
        GameData selectedGame = null;
        if (gameNumberResponse.equalsIgnoreCase("quit")) {
            return "The user quit the operation early.";
        } else {
            selectedGame = availableGames.get(Integer.parseInt(gameNumberResponse)-1);
        }
        try {
            return server.observeGame(selectedGame.gameID(), authToken);
        } catch (ResponseException ex) {
            return "Failed to observe game: " + ex.getMessage();
        }
    }
}