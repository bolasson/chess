package handlers;

import com.google.gson.Gson;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.LogoutResult;
import results.RegisterResult;
import service.UserService;
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
        res.status(result.statusCode());
        return gson.toJson(result);
    };

    public Route login = (Request req, Response res) -> {
        LoginRequest loginRequest = gson.fromJson(req.body(), LoginRequest.class);
        LoginResult result = userService.login(loginRequest);
        res.status(result.statusCode());
        return gson.toJson(result);
    };

    public Route logout = (Request req, Response res) -> {
        String authToken = req.headers("Authorization");
        LogoutResult result = userService.logout(authToken);
        res.status(result.success() ? 200 : 401);
        return gson.toJson(result);
    };
}