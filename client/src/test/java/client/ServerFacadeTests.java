package client;

import model.AuthData;
import model.GameData;
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

//    @Test
//    void registerSuccess() throws Exception {
//        var authData = facade.register("player1", "password", "p1@email.com");
//        assertNotNull(authData.authToken());
//        assertTrue(authData.authToken().length() > 10);
//    }
//
//    @Test
//    void registerFailureDuplicateUser() {
//        Assertions.assertThrows(Exception.class, () -> {
//            facade.register("player1", "password", "p1@email.com");
//            facade.register("player1", "password", "p1@email.com");
//        });
//    }

    @Test
    public void sampleTest() {
        assertTrue(true);
    }
}
