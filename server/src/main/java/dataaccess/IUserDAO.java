package dataaccess;

import model.UserData;

import java.util.List;

public interface IUserDAO {
    void clear() throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void updateUser(UserData user) throws DataAccessException;
    void deleteUser(String username) throws DataAccessException;
    boolean userExists(String username) throws DataAccessException;
    List<UserData> getAllUsers();
}