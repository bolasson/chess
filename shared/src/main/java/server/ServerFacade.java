package server;

import com.google.gson.Gson;
import model.GameData;
import requests.*;
import results.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServerFacade {
    private final String serverURL;
    private static final Gson gson = new Gson();

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
    }

    public LoginResult login(String username, String password) throws ResponseException {
        LoginRequest req = new LoginRequest(username, password);
        LoginResult result = makeRequest("POST", "/session", req, LoginResult.class);
        if (!result.success()) {
            throw new ResponseException(result.statusCode(), result.message());
        }
        return result;
    }

    public RegisterResult register(String username, String password, String email) throws ResponseException {
        RegisterRequest req = new RegisterRequest(username, password, email);
        RegisterResult result = makeRequest("POST", "/user", req, RegisterResult.class);
        if (!result.success()) {
            throw new ResponseException(result.statusCode(), result.message());
        }
        return result;
    }

    public LogoutResult logout(String authToken) throws ResponseException {
        LogoutResult result = makeRequest("DELETE", "/session", null, LogoutResult.class, authToken);
        if (!result.success()) {
            throw new ResponseException(400, result.message());
        }
        return result;
    }

    public CreateGameResult createGame(String gameName, String authToken) throws ResponseException {
        CreateGameRequest req = new CreateGameRequest(authToken, gameName);
        CreateGameResult result = makeRequest("POST", "/game", req, CreateGameResult.class, authToken);
        if (!result.success()) {
            throw new ResponseException(400, result.message());
        }
        return result;
    }

    public List<String> listGames(String authToken) throws ResponseException {
        ListGamesResult result = makeRequest("GET", "/game", null, ListGamesResult.class, authToken);
        if (!result.success()) {
            throw new ResponseException(400, result.message());
        }
        List<String> games = new ArrayList<>();
        int entryID = 0;
        for (GameData game : result.games()) {
            entryID++;
            games.add(entryID + ": " + game.gameName() + "\n- White Player: " + game.whiteUsername() + "\n- Black Player: " + game.blackUsername());
        }

        return games;
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