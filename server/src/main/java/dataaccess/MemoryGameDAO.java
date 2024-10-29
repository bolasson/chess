package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDAO implements IGameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();

    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {
        if (games.containsKey(game.gameID())) {
            throw new DataAccessException("Game already exists");
        }
        games.put(game.gameID(), game);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        GameData game = games.get(gameID);
        if (game == null) {
            throw new DataAccessException("Game not found");
        }
        return game;
    }

    @Override
    public GameData getGameByName(String gameName) throws DataAccessException {
        for (GameData game : games.values()) {
            if (game.gameName().equals(gameName)) {
                return game;
            }
        }
        throw new DataAccessException("Game not found");
    }

    @Override
    public List<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        if (!games.containsKey(game.gameID())) {
            throw new DataAccessException("Game not found");
        }
        games.put(game.gameID(), game);
    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {
        if (!games.containsKey(gameID)) {
            throw new DataAccessException("Game not found");
        }
        games.remove(gameID);
    }

    @Override
    public int countGames() {
        return games.size();
    }
}
