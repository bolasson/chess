package client;

import model.GameData;
import org.junit.jupiter.api.*;
import results.*;
import server.ResponseException;
import server.Server;
import server.ServerFacade;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static String serverURL;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        serverURL = "http://localhost:" + port;
        serverFacade = new ServerFacade(serverURL);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearDatabase() throws Exception {
        URI uri = new URI(serverURL + "/db");
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setDoOutput(true);
        connection.connect();
        int responseCode = connection.getResponseCode();
        assertEquals(2, responseCode / 100, "Database clear failed, response code: " + responseCode);
    }

    @Test
    public void registerSuccess() throws ResponseException {
        RegisterResult result = serverFacade.register("user1", "tooManySecrets", "user1@example.com");
        assertTrue(result.success(), "Expected registration to succeed");
        assertNotNull(result.authToken(), "Expected a non-null authToken");
    }

    @Test
    public void registerFailureEmptyUsername() {
        ResponseException ex = assertThrows(ResponseException.class, () -> serverFacade.register("", "tooManySecrets", "user1@example.com"));
        assertTrue(ex.getMessage().toLowerCase().contains("username"), "Expected error message about username");
    }

    @Test
    public void loginSuccess() throws ResponseException {
        serverFacade.register("user1", "tooManySecrets", "user1@example.com");
        LoginResult loginResult = serverFacade.login("user1", "tooManySecrets");
        assertTrue(loginResult.success(), "Expected login to succeed");
        assertNotNull(loginResult.authToken(), "Expected a valid authToken");
        assertEquals("user1", loginResult.username(), "Expected username to match");
    }

    @Test
    public void loginFailureWrongPassword() throws ResponseException {
        serverFacade.register("user1", "tooManySecrets", "user1@example.com");
        ResponseException ex = assertThrows(ResponseException.class, () -> serverFacade.login("user1", "tooFewSecrets"));
        assertTrue(ex.getMessage().toLowerCase().contains("invalid"), "Expected error message about invalid credentials");
    }

    @Test
    public void logoutSuccess() throws ResponseException {
        RegisterResult reg = serverFacade.register("user1", "tooManySecrets", "user1@example.com");
        String authToken = reg.authToken();
        LogoutResult result = serverFacade.logout(authToken);
        assertTrue(result.success(), "Expected logout to succeed");
    }

    @Test
    public void logoutFailureInvalidAuth() throws ResponseException {
        ResponseException ex = assertThrows(ResponseException.class, () -> serverFacade.logout("invalidAuthToken"));
        assertTrue(ex.getMessage().toLowerCase().contains("not found"), "Expected error message about missing auth token");
    }

    @Test
    public void createGameSuccess() throws ResponseException {
        RegisterResult reg = serverFacade.register("user1", "tooManySecrets", "user1@example.com");
        String authToken = reg.authToken();
        CreateGameResult result = serverFacade.createGame("Test Game", authToken);
        assertTrue(result.success(), "Expected game creation to succeed");
        assertTrue(result.gameID() > 0, "Expected a valid game ID");
    }

    @Test
    public void createGameFailureInvalidAuth() {
        ResponseException ex = assertThrows(ResponseException.class, () -> serverFacade.createGame("Invalid Auth Game", "invalidAuthToken"));
        assertTrue(ex.getMessage().toLowerCase().contains("not found"), "Expected unauthorized error message for createGame with invalid auth token");
    }

    @Test
    public void listGamesSuccess() throws ResponseException {
        RegisterResult reg = serverFacade.register("user1", "tooManySecrets", "user1@example.com");
        String authToken = reg.authToken();
        serverFacade.createGame("Test Game", authToken);
        ListGamesResult result = serverFacade.listGames(authToken);
        assertTrue(result.success(), "Expected listing games to succeed");
        List<GameData> games = result.games();
        assertNotNull(games, "Expected a non-null list of games");
        assertFalse(games.isEmpty(), "Expected at least one game in the list");
    }

    @Test
    public void listGamesFailureInvalidAuth() {
        ResponseException ex = assertThrows(ResponseException.class, () -> serverFacade.listGames("invalidAuthToken"));
        assertTrue(ex.getMessage().toLowerCase().contains("unauthorized"), "Expected unauthorized error message for listGames with invalid auth token");
    }

    @Test
    public void joinGameSuccess() throws ResponseException {
        RegisterResult reg = serverFacade.register("user1", "tooManySecrets", "user1@example.com");
        String authToken = reg.authToken();
        CreateGameResult gameResult = serverFacade.createGame("Test Game", authToken);
        int gameId = gameResult.gameID();
        JoinGameResult joinResult = serverFacade.joinGame(gameId, "white", authToken);
        assertTrue(joinResult.success(), "Expected joining the game to succeed");
    }

    @Test
    public void joinGameFailureInvalidColor() throws ResponseException {
        RegisterResult reg = serverFacade.register("user1", "tooManySecrets", "user1@example.com");
        String authToken = reg.authToken();
        CreateGameResult gameResult = serverFacade.createGame("Test Game", authToken);
        int gameId = gameResult.gameID();
        ResponseException ex = assertThrows(ResponseException.class, () -> serverFacade.joinGame(gameId, "byublue", authToken));
        assertTrue(ex.getMessage().toLowerCase().contains("color"), "Expected error message regarding invalid color");
    }

    @Test
    public void observeGameSuccess() throws ResponseException {
        RegisterResult reg = serverFacade.register("user1", "tooManySecrets", "user1@example.com");
        String authToken = reg.authToken();
        CreateGameResult gameResult = serverFacade.createGame("Test Game", authToken);
        String response = serverFacade.observeGame(gameResult.gameID(), "Test Game", authToken);
        assertTrue(response.contains("Observing the game"), "Expected observe game message to contain observing info");
    }

    @Test
    public void observeGameFailureUnauthorized() {
        ResponseException ex = assertThrows(ResponseException.class, () -> serverFacade.observeGame(1, "Any Game", ""));
        assertTrue(ex.getMessage().toLowerCase().contains("unauthorized"), "Expected error message about unauthorized operation");
    }
}
