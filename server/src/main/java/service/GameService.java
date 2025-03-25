package service;

import dataaccess.DataAccessException;
import dataaccess.IAuthDAO;
import dataaccess.IGameDAO;
import model.AuthData;
import model.GameData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import results.CreateGameResult;
import results.JoinGameResult;
import results.ListGamesResult;

import java.util.List;

public class GameService {
    private final IGameDAO gameDAO;
    private final IAuthDAO authDAO;

    public GameService(IGameDAO gameDAO, IAuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public void clear() throws DataAccessException {
        gameDAO.clear();
    }

    public CreateGameResult createGame(CreateGameRequest request) {
        try {
            AuthData authData = authDAO.getAuth(request.authToken());
            if (authData == null) {
                return new CreateGameResult(false, request.gameName(), "Unauthorized");
            }
            int gameId = generateGameId();
            GameData game = new GameData(gameId, null, null, request.gameName(), null);
            gameDAO.createGame(game);
            return new CreateGameResult(true, request.gameName(), gameId);
        } catch (Exception e) {
            return new CreateGameResult(false,request.gameName(), e.getMessage());
        }
    }

    public ListGamesResult listGames(String authToken) {
        try {
            authDAO.getAuth(authToken);
            List<GameData> games = gameDAO.listGames();
            return new ListGamesResult(true, games);
        } catch (DataAccessException e) {
            return new ListGamesResult(false, "Unauthorized");
        }
    }

    public JoinGameResult joinGame(JoinGameRequest request) {
        JoinGameResult result = new JoinGameResult(false, "An unexpected server error occurred", 500);
        try {
            result = new JoinGameResult(false, "Unauthorized", 401);
            AuthData authData = authDAO.getAuth(request.authToken());
            result = new JoinGameResult(false, "Game not found", 400);
            GameData game = gameDAO.getGame(request.gameID());
            if (request.playerColor() == null) {
                return new JoinGameResult(false, "Player color is required", 400);
            }
            if (request.gameID() <= 0) {
                return new JoinGameResult(false, "GameID is required", 400);
            }
            if (request.playerColor().equals("WHITE") && game.whiteUsername() == null) {
                if (game.blackUsername() != null && game.blackUsername().equals(authData.username())) {
                    return new JoinGameResult(false, "User already joined as the black player", 400);
                }
                game = new GameData(game.gameID(), authData.username(), game.blackUsername(), game.gameName(), game.game());
            } else if (request.playerColor().equals("BLACK") && game.blackUsername() == null) {
                System.out.println(game.whiteUsername());
                if (game.whiteUsername() != null && game.whiteUsername().equals(authData.username())) {
                    return new JoinGameResult(false, "User already joined as the white player", 400);
                }
                game = new GameData(game.gameID(), game.whiteUsername(), authData.username(), game.gameName(), game.game());
            } else if (!request.playerColor().equals("WHITE") && !request.playerColor().equals("BLACK")) {
                return new JoinGameResult(false, "Color is not valid", 400);
            } else {
                return new JoinGameResult(false, "Color already taken", 403);
            }
            gameDAO.updateGame(game);
            return new JoinGameResult(true);
        } catch (DataAccessException e) {
            return result;
        }
    }

    private int generateGameId() {
        return (int) (Math.random() * 100000);
    }
}