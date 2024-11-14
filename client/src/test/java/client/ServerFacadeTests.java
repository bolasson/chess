package client;

import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
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
        Assertions.assertTrue(true);
    }

}
