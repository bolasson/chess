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
}