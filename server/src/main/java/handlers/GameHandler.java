package handlers;

import com.google.gson.Gson;
import service.GameService;
import service.CreateGameRequest;
import service.CreateGameResult;
import service.JoinGameRequest;
import service.JoinGameResult;
import service.ListGamesResult;
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
        CreateGameRequest createGameRequest = gson.fromJson(req.body(), CreateGameRequest.class);
        CreateGameResult result = gameService.createGame(createGameRequest);
        res.status(result.success() ? 200 : 400);
        return gson.toJson(result);
    };

    public Route listGames = (Request req, Response res) -> {
        String authToken = req.headers("Authorization");
        ListGamesResult result = gameService.listGames(authToken);
        res.status(result.success() ? 200 : 401);
        return gson.toJson(result);
    };

    public Route joinGame = (Request req, Response res) -> {
        JoinGameRequest joinGameRequest = gson.fromJson(req.body(), JoinGameRequest.class);
        JoinGameResult result = gameService.joinGame(joinGameRequest);
        res.status(result.success() ? 200 : 400);
        return gson.toJson(result);
    };
}