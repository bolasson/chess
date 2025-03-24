package client;

public class Client {

    private enum State {
        PRELOGIN,
        POSTLOGIN
    }

    private State currentState;
    private final String serverURL;
    private ServerFacade server;

    public Client(String serverURL) {
        this.server = new ServerFacade(serverURL);
        this.serverURL = serverURL;
        this.currentState = State.PRELOGIN;
    }

    public String evaluateCommand(String input, java.util.Scanner scanner) {
        String command = input.trim().toLowerCase();
        if (currentState == State.PRELOGIN) {
            switch (command) {
                case "help":
                    return getHelpText();
                case "quit":
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
                case "help":
                    return getHelpText();
                case "quit":
                    return "";
                case "logout":
                    return logout();
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

    private String login(java.util.Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String response = server.login(username, password);

        currentState = State.POSTLOGIN;
        return response + "Type 'help' to see available commands.";
    }


    private String register(java.util.Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        String response = server.register(username, password, email);

        currentState = State.POSTLOGIN;
        return response + "Type 'help' to see available commands.";
    }

    private String logout() {
        currentState = State.PRELOGIN;
        return server.logout("authToken") + "Type 'help' to see available commands.";
    }
}