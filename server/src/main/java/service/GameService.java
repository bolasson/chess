package service;

import dataaccess.IAuthDAO;
import dataaccess.DataAccessException;
import dataaccess.IGameDAO;
import model.AuthData;
import model.GameData;
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
                return new CreateGameResult(false, "Error: unauthorized");
            }
            int gameId = generateGameId();
            GameData game = new GameData(gameId, null, null, request.gameName(), null);
            gameDAO.createGame(game);
            return new CreateGameResult(true, gameId);
        } catch (DataAccessException e) {
            return new CreateGameResult(false, "Error: " + e.getMessage());
        }
    }

    public ListGamesResult listGames(String authToken) {
        try {
            authDAO.getAuth(authToken);
            List<GameData> games = gameDAO.listGames();
            return new ListGamesResult(true, games);
        } catch (DataAccessException e) {
            return new ListGamesResult(false, "Error: unauthorized");
        }
    }

    public JoinGameResult joinGame(JoinGameRequest request) {
        try {
            AuthData authData = authDAO.getAuth(request.authToken());
            if (authData == null) {
                return new JoinGameResult(false, "Error: unauthorized");
            }
            GameData game = gameDAO.getGame(request.gameID());
            if (request.playerColor().equals("WHITE") && game.whiteUsername() == null) {
                game = new GameData(game.gameID(), authData.username(), game.blackUsername(), game.gameName(), game.game());
            } else if (request.playerColor().equals("BLACK") && game.blackUsername() == null) {
                game = new GameData(game.gameID(), game.whiteUsername(), authData.username(), game.gameName(), game.game());
            } else {
                return new JoinGameResult(false, "Error: color already taken");
            }
            gameDAO.updateGame(game);
            return new JoinGameResult(true);
        } catch (DataAccessException e) {
            return new JoinGameResult(false, "Error: " + e.getMessage());
        }
    }

    private int generateGameId() {
        return (int) (Math.random() * 100000);
    }
}