package server;

import com.google.gson.Gson;
import requests.*;
import results.*;

import java.io.*;
import java.net.*;

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

    public void logout(String authToken) throws ResponseException {
        LogoutResult result = makeRequest("DELETE", "/session", null, LogoutResult.class, authToken);
        if (!result.success()) {
            throw new ResponseException(400, result.message());
        }
    }

    public CreateGameResult createGame(String gameName, String authToken) throws ResponseException {
        CreateGameRequest req = new CreateGameRequest(authToken, gameName);
        CreateGameResult result = makeRequest("POST", "/game", req, CreateGameResult.class, authToken);
        if (!result.success()) {
            throw new ResponseException(400, result.message());
        }
        return result;
    }

    public ListGamesResult listGames(String authToken) throws ResponseException {
        ListGamesResult result = makeRequest("GET", "/game", null, ListGamesResult.class, authToken);
        if (!result.success()) {
            throw new ResponseException(400, result.message());
        }

        return result;
    }

    public void joinGame(int gameId, String color, String authToken) throws ResponseException {
        JoinGameRequest req = new JoinGameRequest(authToken, gameId, color.toUpperCase());
        JoinGameResult result = makeRequest("PUT", "/game", req, JoinGameResult.class, authToken);
        if (!result.success()) {
            throw new ResponseException(result.statusCode(), result.message());
        }
    }

    public String observeGame(int gameID, String gameName, String authToken) throws ResponseException {
        if (authToken.isEmpty()) {
            throw new ResponseException(401, "You are unauthorized to perform this operation");
        }
        return "Observing the game '" + gameName + "' with gameID '" + gameID + "'";
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
                return gson.fromJson(reader, responseClass);
            }
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}