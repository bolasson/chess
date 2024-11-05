package handlers;

import com.google.gson.Gson;
import service.UserService;
import service.RegisterRequest;
import service.RegisterResult;
import service.LoginRequest;
import service.LoginResult;
import service.LogoutResult;
import spark.Request;
import spark.Response;
import spark.Route;

public class UserHandler {
    private UserService userService = null;
    private final Gson gson = new Gson();

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Route register = (Request req, Response res) -> {
        RegisterRequest registerRequest = gson.fromJson(req.body(), RegisterRequest.class);
        RegisterResult result = userService.register(registerRequest);
        res.status(result.success() ? 200 : 400);
        return gson.toJson(result);
    };

    public Route login = (Request req, Response res) -> {
        LoginRequest loginRequest = gson.fromJson(req.body(), LoginRequest.class);
        LoginResult result = userService.login(loginRequest);
        res.status(result.success() ? 200 : 401);
        return gson.toJson(result);
    };

    public Route logout = (Request req, Response res) -> {
        String authToken = req.headers("Authorization");
        LogoutResult result = userService.logout(authToken);
        res.status(result.success() ? 200 : 401);
        return gson.toJson(result);
    };
}