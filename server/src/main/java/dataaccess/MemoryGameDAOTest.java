package dataaccess;

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

}
