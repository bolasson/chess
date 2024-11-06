package dataaccess;

import model.UserData;

public class SQLUserDAO implements IUserDAO {

    @Override
    public void clear() throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean userExists(String username) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean verifyUserPassword(String username, String providedPassword) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
