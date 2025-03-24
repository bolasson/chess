package client;

public class ServerFacade {
    private final String serverURL;

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
    }
    
    public String login(String username, String password) {
        return "Login successful for user: " + username;
    }

    public String register(String username, String password, String email) {
        return "Registration successful for user: " + username;
    }

    public String logout(String authToken) {
        return "Logout successful.";
    }

    public String createGame(String gameName, String authToken) {
        return "Game created with name: " + gameName;
    }

    public String listGames(String authToken) {
        return "Games: [Game1, Game2, Game3]";
    }

    public String joinGame(int gameId, String color, String authToken) {
        return "Join game with gameID " + gameId + " as " + color;
    }
    
    public String observeGame(int gameID, String authToken) {
        return "Observe game with gameID " + gameID;
    }
}