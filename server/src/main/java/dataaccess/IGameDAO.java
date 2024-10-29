package dataaccess;

import model.GameData;
import java.util.List;

public interface IGameDAO {
    void clear() throws DataAccessException;
    void createGame(GameData game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
//    GameData getGameByName(String gameName) throws DataAccessException;
//    List<GameData> listGames() throws DataAccessException;
//    List<GameData> getGamesByUser(String username) throws DataAccessException;
//    void updateGame(GameData game) throws DataAccessException;
//    void deleteGame(int gameID) throws DataAccessException;
//    int countGames() throws DataAccessException;
}