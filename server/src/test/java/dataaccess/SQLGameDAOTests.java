package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTests {
    private SQLGameDAO gameDAO;
    private SQLUserDAO userDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDAO = new SQLGameDAO();
        userDAO = new SQLUserDAO();
        DatabaseManager.initializeDatabase();
        gameDAO.clear();
        userDAO.clear();
        userDAO.createUser(new UserData("whitePlayer", "password123", "white@example.com"));
        userDAO.createUser(new UserData("blackPlayer", "password123", "black@example.com"));
    }

    @AfterEach
    public void clearData() throws DataAccessException {
        gameDAO.clear();
        userDAO.clear();
    }

    @Test
    public void clearGames() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        gameDAO.createGame(new GameData(1, "whitePlayer", "blackPlayer", "Test Game", chessGame));
        assertNotNull(gameDAO.getGame(1));
        gameDAO.clear();
        assertNull(gameDAO.getGame(1));
    }

    @Test
    public void createGameSuccess() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(1, "whitePlayer", "blackPlayer", "Test Game", chessGame);
        gameDAO.createGame(game);
        GameData retrievedGame = gameDAO.getGame(1);
        assertNotNull(retrievedGame);
        assertEquals("Test Game", retrievedGame.gameName());
        assertEquals("whitePlayer", retrievedGame.whiteUsername());
        assertEquals("blackPlayer", retrievedGame.blackUsername());
    }

    @Test
    public void createGameFailureInvalidUser() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData invalidGame = new GameData(1, "nonExistentUser", "blackPlayer", "Invalid Game", chessGame);
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(invalidGame));
    }

    @Test
    public void getGameSuccess() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(1, "whitePlayer", "blackPlayer", "Test Game", chessGame);
        gameDAO.createGame(game);
        GameData retrievedGame = gameDAO.getGame(1);
        assertNotNull(retrievedGame);
        assertEquals("Test Game", retrievedGame.gameName());
    }

    @Test
    public void getGameFailureNotFound() throws DataAccessException {
        GameData retrievedGame = gameDAO.getGame(999999999);
        assertNull(retrievedGame);
    }

    @Test
    public void listGamesSuccess() throws DataAccessException {
        ChessGame chessGame1 = new ChessGame();
        ChessGame chessGame2 = new ChessGame();
        gameDAO.createGame(new GameData(1, "whitePlayer", null, "Game 1", chessGame1));
        gameDAO.createGame(new GameData(2, null, "blackPlayer", "Game 2", chessGame2));
        List<GameData> games = gameDAO.listGames();
        assertEquals(2, games.size());
    }

    @Test
    public void listGamesFailureNoGamesExist() throws DataAccessException {
        gameDAO.clear();
        List<GameData> games = gameDAO.listGames();
        assertEquals(0, games.size(), "Expected an empty game list when no games exist");
    }

    @Test
    public void updateGameSuccess() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(1, "whitePlayer", "blackPlayer", "Test Game", chessGame);
        gameDAO.createGame(game);
        GameData updatedGame = new GameData(1, "whitePlayer", null, "Updated Game", chessGame);
        gameDAO.updateGame(updatedGame);
        GameData retrievedGame = gameDAO.getGame(1);
        assertNotNull(retrievedGame);
        assertEquals("Updated Game", retrievedGame.gameName());
        assertNull(retrievedGame.blackUsername());
    }

    @Test
    public void updateGameFailureInvalidGame() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(1, "whitePlayer", "blackPlayer", "Test Game", chessGame);
        assertThrows(DataAccessException.class, () -> gameDAO.updateGame(game));
    }
}
