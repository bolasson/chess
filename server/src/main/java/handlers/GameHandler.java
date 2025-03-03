package handlers;

import com.google.gson.Gson;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import results.CreateGameResult;
import results.JoinGameResult;
import results.ListGamesResult;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class GameHandler {
    private GameService gameService;
    private final Gson gson = new Gson();

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Route createGame = (Request req, Response res) -> {
        String authToken = req.headers("Authorization");
        CreateGameRequest createGameRequest = gson.fromJson(req.body(), CreateGameRequest.class);
        createGameRequest = new CreateGameRequest(authToken, createGameRequest.gameName());
        CreateGameResult result = gameService.createGame(createGameRequest);
        res.status(result.success() ? 200 : 401);
        return gson.toJson(result);
    };

    public Route listGames = (Request req, Response res) -> {
        String authToken = req.headers("Authorization");
        ListGamesResult result = gameService.listGames(authToken);
        res.status(result.success() ? 200 : 401);
        return gson.toJson(result);
    };

    public Route joinGame = (Request req, Response res) -> {
        String authToken = req.headers("Authorization");
        JoinGameRequest joinGameRequest = gson.fromJson(req.body(), JoinGameRequest.class);
        joinGameRequest = new JoinGameRequest(authToken, joinGameRequest.gameID(), joinGameRequest.playerColor());
        JoinGameResult result = gameService.joinGame(joinGameRequest);
        res.status(result.statusCode());
        return gson.toJson(result);
    };
}