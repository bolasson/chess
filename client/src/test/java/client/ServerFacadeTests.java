package client;

import server.Server;
import serverfacade.ServerFacade;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearData() throws Exception {
        facade.clearDatabase();
    }

    @Test
    void registerSuccess() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertNotNull(authData.authToken());
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerFailureDuplicateUser() {
        Assertions.assertThrows(Exception.class, () -> {
            facade.register("player1", "password", "p1@email.com");
            facade.register("player1", "password", "p1@email.com");
        });
    }

    @Test
    public void loginSuccess() throws Exception {
        facade.register("loginUser", "password123", "loginUser@example.com");
        var authData = facade.login("loginUser", "password123");
        assertNotNull(authData);
        assertEquals("loginUser", authData.username());
    }

    @Test
    public void loginFailureInvalidCredentials() {
        Exception exception = assertThrows(Exception.class, () ->
                facade.login("nonExistentUser", "wrongPassword")
        );
        assertTrue(exception.getMessage().contains("user does not exist"));
    }

    @Test
    public void testLogout() throws Exception {
        assertThrows(UnsupportedOperationException.class, () -> {
            facade.logout("authToken");
        });
    }

    @Test
    public void testCreateGame() throws Exception {
        assertThrows(UnsupportedOperationException.class, () -> {
            facade.createGame("Test Game", "authToken");
        });
    }

    @Test
    public void testListGames() throws Exception {
        assertThrows(UnsupportedOperationException.class, () -> {
            facade.listGames("authToken");
        });
    }

    @Test
    public void testJoinGame() throws Exception {
        assertThrows(UnsupportedOperationException.class, () -> {
            facade.joinGame(1, "WHITE", "authToken");
        });
    }

    @Test
    public void testObserveGame() throws Exception {
        assertThrows(UnsupportedOperationException.class, () -> {
            facade.observeGame(1, "authToken");
        });
    }
}
