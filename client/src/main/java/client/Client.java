package client;

public class Client {

    private enum State {
        PRELOGIN,
        POSTLOGIN
    }

    private State currentState;
    private final String serverURL;

    public Client(String serverURL) {
        this.serverURL = serverURL;
        this.currentState = State.PRELOGIN;
    }

    public String evaluateCommand(String input) {
        String command = input.trim().toLowerCase();
        switch (command) {
            case "help":
                return getHelpText();
            case "quit":
                return "";
            case "login":
                return login();
            case "register":
                return register();
            case "logout":
                return logout();
            default:
                return "Unknown command. Type 'help' for available commands.";
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

    private String login() {
        currentState = State.POSTLOGIN;
        return "Logged in successfully. Type 'help' to see available commands.";
    }

    private String register() {
        currentState = State.POSTLOGIN;
        return "Registered and logged in successfully. Type 'help' to see available commands.";
    }

    private String logout() {
        currentState = State.PRELOGIN;
        return "Logged out successfully. Type 'help' to see available commands.";
    }
}