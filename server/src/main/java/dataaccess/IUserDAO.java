package dataaccess;

import model.UserData;

public interface IUserDAO {
    void clear() throws DataAccessException;
    void insertUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void updateUser(UserData user) throws DataAccessException;
    void deleteUser(String username) throws DataAccessException;
    boolean userExists(String username) throws DataAccessException;
}