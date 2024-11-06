package dataaccess;

import model.GameData;

import java.util.List;

public class SQLGameDAO implements IGameDAO {
    @Override
    public void clear() throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
