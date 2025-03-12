package server;

import dataaccess.*;
import dataaccess.sql.SQLAuthDAO;
import dataaccess.sql.SQLGameDAO;
import dataaccess.sql.SQLUserDAO;
import handlers.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.Spark;

public class Server {

    public int run(int desiredPort) {
        try {
            DatabaseManager.initializeDatabase();
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            return -1;
        }

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        IUserDAO userDAO = new SQLUserDAO();
        IAuthDAO authDAO = new SQLAuthDAO();
        IGameDAO gameDAO = new SQLGameDAO();
        UserService userService = new UserService(userDAO, authDAO);
        GameService gameService = new GameService(gameDAO, authDAO);
        AuthService authService = new AuthService(authDAO);
        UserHandler userHandler = new UserHandler(userService);
        GameHandler gameHandler = new GameHandler(gameService);
        ClearHandler clearHandler = new ClearHandler(userService, gameService, authService);
        Spark.post("/user", userHandler.register);
        Spark.post("/session", userHandler.login);
        Spark.delete("/session", userHandler.logout);
        Spark.post("/game", gameHandler.createGame);
        Spark.get("/game", gameHandler.listGames);
        Spark.put("/game", gameHandler.joinGame);
        Spark.delete("/db", clearHandler.clear);
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
