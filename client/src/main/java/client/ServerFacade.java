package client;

import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.util.List;
import model.GameData;

public class ServerFacade {
    private final String serverURL;
    private static final Gson gson = new Gson();

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

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        return makeRequest(method, path, request, responseClass, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URI uri = new URI(serverURL + path);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            if (authToken != null) {
                connection.setRequestProperty("Authorization", authToken);
            }
            if (request != null) {
                connection.setRequestProperty("Content-Type", "application/json");
                String jsonRequest = gson.toJson(request);
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(jsonRequest.getBytes());
                }
            }
            connection.connect();
            int status = connection.getResponseCode();
            InputStream is = (status >= 200 && status < 300)
                    ? connection.getInputStream()
                    : connection.getErrorStream();
            try (Reader reader = new InputStreamReader(is)) {
                T response = gson.fromJson(reader, responseClass);
                return response;
            }
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}