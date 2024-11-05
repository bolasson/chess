package server;

import spark.*;
import com.google.gson.Gson;
import dataaccess.*;
import handlers.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.Spark;


public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        UserService userService = new UserService(userDAO, authDAO);
        GameService gameService = new GameService(gameDAO, authDAO);
        UserHandler userHandler = new UserHandler(userService);
        GameHandler gameHandler = new GameHandler(gameService);
        Spark.post("/user", userHandler.register);
        Spark.post("/session", userHandler.login);
        Spark.delete("/session", userHandler.logout);
        Spark.post("/game", gameHandler.createGame);
        Spark.get("/game", gameHandler.listGames);
        Spark.put("/game", gameHandler.joinGame);
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
