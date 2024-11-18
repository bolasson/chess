package serverfacade;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.util.List;

public class ServerFacade {
    private final String serverUrl;
    private final Gson gson = new Gson();

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
    }

    public AuthData register(String username, String password, String email) throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public AuthData login(String username, String password) throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void logout(String authToken) throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public GameData createGame(String gameName, String authToken) throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<GameData> listGames(String authToken) throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void joinGame(int gameId, String teamColor, String authToken) throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void observeGame(int gameId, String authToken) throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
