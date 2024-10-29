package dataaccess;

import model.AuthData;

public interface IAuthDAO {
    void createAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    AuthData getAuthByUsername(String username) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    boolean authExists(String authToken) throws DataAccessException;
}