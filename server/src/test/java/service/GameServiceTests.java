package service;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.results.CreateGameResult;
import service.results.JoinGameResult;
import service.results.ListGamesResult;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    private GameService gameService;
    private IGameDAO gameDAO;
    private IAuthDAO authDAO;

    @BeforeEach
    public void setUp() {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        gameService = new GameService(gameDAO, authDAO);
    }

    @Test
    public void clear() throws DataAccessException {
        GameData game1 = new GameData(1, "whitePlayer1", "blackPlayer1", "Game1", null);
        GameData game2 = new GameData(2, "whitePlayer2", "blackPlayer2", "Game2", null);
        gameDAO.createGame(game1);
        gameDAO.createGame(game2);
        gameService.clear();
        assertThrows(DataAccessException.class, () -> gameDAO.getGame(1));
        assertThrows(DataAccessException.class, () -> gameDAO.getGame(2));
    }

    @Test
    public void createGameSuccess() throws DataAccessException {
        AuthData authData = new AuthData("authToken1", "user1");
        authDAO.createAuth(authData);
        CreateGameRequest request = new CreateGameRequest("authToken1", "Game1");
        CreateGameResult result = gameService.createGame(request);
        assertTrue(result.success());
        assertNotEquals(-1, result.gameID());
        GameData game = gameDAO.getGame(result.gameID());
        assertEquals("Game1", game.gameName());
    }

    @Test
    public void createGameFailureUnauthorized() {
        CreateGameRequest request = new CreateGameRequest("invalidAuthToken", "Game1");
        CreateGameResult result = gameService.createGame(request);
        assertFalse(result.success());
        assertEquals("Error: Auth token not found", result.message());
    }

    @Test
    public void listGamesSuccess() throws DataAccessException {
        AuthData authData = new AuthData("authToken1", "user1");
        authDAO.createAuth(authData);
        gameDAO.createGame(new GameData(1, "user1", null, "Game1", null));
        gameDAO.createGame(new GameData(2, "user2", null, "Game2", null));
        ListGamesResult result = gameService.listGames("authToken1");
        assertTrue(result.success());
        assertEquals(2, result.games().size());
    }

    @Test
    public void listGamesFailureUnauthorized() {
        ListGamesResult result = gameService.listGames("invalidAuthToken");
        assertFalse(result.success());
        assertEquals("Error: unauthorized", result.message());
    }

    @Test
    public void joinGameSuccessWhite() throws DataAccessException {
        AuthData authData = new AuthData("authToken1", "user1");
        authDAO.createAuth(authData);
        GameData game = new GameData(1, null, null, "Game1", null);
        gameDAO.createGame(game);
        JoinGameRequest request = new JoinGameRequest("authToken1", 1, "WHITE");
        JoinGameResult result = gameService.joinGame(request);
        assertTrue(result.success());
        GameData updatedGame = gameDAO.getGame(1);
        assertEquals("user1", updatedGame.whiteUsername());
        assertNull(updatedGame.blackUsername());
    }

    @Test
    public void joinGameFailureAlreadyTaken() throws DataAccessException {
        AuthData authData = new AuthData("authToken1", "user1");
        authDAO.createAuth(authData);
        GameData game = new GameData(1, "anotherUser", null, "Game1", null);
        gameDAO.createGame(game);
        JoinGameRequest request = new JoinGameRequest("authToken1", 1, "WHITE");
        JoinGameResult result = gameService.joinGame(request);
        assertFalse(result.success());
        assertEquals("Error: color already taken", result.message());
    }

    @Test
    public void joinGameFailureUnauthorized() {
        JoinGameRequest request = new JoinGameRequest("invalidAuthToken", 1, "WHITE");
        JoinGameResult result = gameService.joinGame(request);
        assertFalse(result.success());
        assertEquals("Error: Auth token not found", result.message());
    }
}