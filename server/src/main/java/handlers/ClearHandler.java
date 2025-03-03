package handlers;

import service.AuthService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler {
    private UserService userService;
    private GameService gameService;
    private AuthService authService;

    public ClearHandler(UserService userService, GameService gameService, AuthService authService) {
        this.userService = userService;
        this.gameService = gameService;
        this.authService = authService;
    }

    public Route clear = (Request req, Response res) -> {
        try {
            userService.clear();
            gameService.clear();
            authService.clear();
            res.status(200);
            return "{}";
        } catch (Exception e) {
            res.status(500);
            return "{ \"message\": \"Error: " + e.getMessage() + "\" }";
        }
    };
}