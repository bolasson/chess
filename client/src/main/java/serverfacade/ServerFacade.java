package serverfacade;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import requests.RegisterRequest;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ServerFacade {
    private final String serverUrl;
    private final Gson gson = new Gson();

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
    }

    public void clearDatabase() throws Exception {
        URL url = new URL(serverUrl + "/db");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setDoOutput(true);
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("Failed to clear database. Server returned status code: " + responseCode);
        }
        connection.disconnect();
    }

    public AuthData register(String username, String password, String email) throws Exception {
        URL url = new URL(serverUrl + "/user");
        HttpURLConnection conn = createConnection(url, "POST");
        String body = gson.toJson(new RegisterRequest(username, password, email));
        sendRequest(conn, body);
        if (conn.getResponseCode() == 200) {
            return gson.fromJson(new String(conn.getInputStream().readAllBytes()), AuthData.class);
        } else {
            throw new Exception(readError(conn));
        }
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

    private HttpURLConnection createConnection(URL url, String method) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        return conn;
    }

    private void sendRequest(HttpURLConnection conn, String body) throws Exception {
        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes());
        }
    }

    private String readError(HttpURLConnection conn) throws Exception {
        return new String(conn.getErrorStream().readAllBytes());
    }
}
