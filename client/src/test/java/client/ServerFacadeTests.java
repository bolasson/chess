package client;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import results.*;
import server.ResponseException;
import server.Server;
import server.ServerFacade;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static String baseUrl;
    private static final Gson gson = new Gson();

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        baseUrl = "http://localhost:" + port;
        facade = new ServerFacade(baseUrl);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearDatabase() throws Exception {
        URI uri = new URI(baseUrl + "/db");
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
        RegisterResult result = facade.register("user1", "toomanysecrets", "user1@example.com");
        assertTrue(result.success(), "Expected registration to succeed");
        assertNotNull(result.authToken(), "Expected a non-null authToken");
    }

    @Test
    public void registerFailureEmptyUsername() {
        ResponseException ex = assertThrows(ResponseException.class, () -> facade.register("", "toomanysecrets", "user1@example.com"));
        assertTrue(ex.getMessage().toLowerCase().contains("username"), "Expected error message about username");
    }

    @Test
    public void loginSuccess() throws ResponseException {
        facade.register("user1", "toomanysecrets", "user1@example.com");
        LoginResult loginResult = facade.login("user1", "toomanysecrets");
        assertTrue(loginResult.success(), "Expected login to succeed");
        assertNotNull(loginResult.authToken(), "Expected a valid authToken");
        assertEquals("user1", loginResult.username(), "Expected username to match");
    }

    @Test
    public void loginFailureWrongPassword() throws ResponseException {
        facade.register("user1", "toomanysecrets", "user1@example.com");
        ResponseException ex = assertThrows(ResponseException.class, () -> facade.login("user1", "toofewsecrets"));
        assertTrue(ex.getMessage().toLowerCase().contains("invalid"), "Expected error message about invalid credentials");
    }

    @Test
    public void sampleTest() {
        assertTrue(true);
    }

}
