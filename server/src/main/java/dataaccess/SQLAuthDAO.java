package dataaccess;

import model.AuthData;

public class SQLAuthDAO implements IAuthDAO {

    @Override
    public void clear() throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
