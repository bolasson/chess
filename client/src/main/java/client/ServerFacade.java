package client;

public class ServerFacade {
    private final String serverURL;

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
    }
    
    public String login(String username, String password) {
        return "Login successful for " + username + "\n";
    }

    public String register(String username, String password, String email) {
        return "Registration successful for " + username + "\n";
    }

    public String logout(String authToken) {
        return "Logout successful." + "\n";
    }

    public String createGame(String gameName, String authToken) {
        return "Game created with name " + gameName + "\n";
    }

    public String listGames(String authToken) {
        return "Active games [Game1, Game2, Game3]" + "\n";
    }

    public String joinGame(int gameId, String color, String authToken) {
        return "Joined game with gameID " + gameId + " as " + color + "\n";
    }
    
    public String observeGame(int gameID, String authToken) {
        return "Observing game with gameID " + gameID + "\n";
    }
}