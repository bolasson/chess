package passoff.dataaccess;

import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MemoryGameDAOTest {
    private MemoryGameDAO gameDAO;

    @BeforeEach
    public void setUp() {
        gameDAO = new MemoryGameDAO();
    }

    @Test
    public void testClear() throws DataAccessException {
        GameData game1 = new GameData(1, "whitePlayer1", "blackPlayer1", "Game1", null);
        GameData game2 = new GameData(2, "whitePlayer2", "blackPlayer2", "Game2", null);
        gameDAO.createGame(game1);
        gameDAO.createGame(game2);
        gameDAO.clear();
        assertThrows(DataAccessException.class, () -> gameDAO.getGame(1));
        assertThrows(DataAccessException.class, () -> gameDAO.getGame(2));
    }

    @Test
    public void testCreateGame() throws DataAccessException {
        GameData game = new GameData(1, "whitePlayer", "blackPlayer", "Game1", null);
        gameDAO.createGame(game);
        assertEquals(game, gameDAO.getGame(1));
    }

    @Test
    public void testGetGameByName() throws DataAccessException {
        GameData game = new GameData(1, "whitePlayer", "blackPlayer", "Game1", null);
        gameDAO.createGame(game);
        assertEquals(game, gameDAO.getGameByName("Game1"));
    }

    @Test
    public void testListGames() throws DataAccessException {
        GameData game1 = new GameData(1, "whitePlayer1", "blackPlayer1", "Game1", null);
        GameData game2 = new GameData(2, "whitePlayer2", "blackPlayer2", "Game2", null);
        gameDAO.createGame(game1);
        gameDAO.createGame(game2);

        List<GameData> games = gameDAO.listGames();
        assertEquals(2, games.size());
    }

    @Test
    public void testUpdateGame() throws DataAccessException {
        GameData game = new GameData(1, "whitePlayer", "blackPlayer", "Game1", null);
        gameDAO.createGame(game);

        GameData updatedGame = new GameData(1, "newWhitePlayer", "blackPlayer", "Game1", null);
        gameDAO.updateGame(updatedGame);

        assertEquals(updatedGame, gameDAO.getGame(1));
    }

    @Test
    public void testDeleteGame() throws DataAccessException {
        GameData game = new GameData(1, "whitePlayer", "blackPlayer", "Game1", null);
        gameDAO.createGame(game);

        gameDAO.deleteGame(1);
        assertThrows(DataAccessException.class, () -> gameDAO.getGame(1));
    }

    @Test
    public void testCountGames() throws DataAccessException {
        GameData game1 = new GameData(1, "whitePlayer1", "blackPlayer1", "Game1", null);
        GameData game2 = new GameData(2, "whitePlayer2", "blackPlayer2", "Game2", null);
        gameDAO.createGame(game1);
        gameDAO.createGame(game2);

        assertEquals(2, gameDAO.countGames());
    }
}
