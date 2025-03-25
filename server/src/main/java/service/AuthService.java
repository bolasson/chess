package service;

import dataaccess.DataAccessException;
import dataaccess.IAuthDAO;
import model.AuthData;
import results.DeactivationResult;
import results.GenerationResult;
import results.VerificationResult;

public class AuthService {
    private final IAuthDAO authDAO;

    public AuthService(IAuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void clear() throws DataAccessException {
        authDAO.clear();
    }

    public VerificationResult verifyAuthToken(String authToken) {
        try {
            AuthData authData = authDAO.getAuth(authToken);
            return new VerificationResult(true, authData.username());
        } catch (DataAccessException e) {
            return new VerificationResult(false, "Error: Invalid auth token");
        }
    }

    public GenerationResult generateNewAuthToken(String username) {
        try {
            String newAuthToken = java.util.UUID.randomUUID().toString();
            AuthData authData = new AuthData(newAuthToken, username);
            authDAO.createAuth(authData);
            return new GenerationResult(true, newAuthToken);
        } catch (DataAccessException e) {
            return new GenerationResult(false, e.getMessage());
        }
    }

    public DeactivationResult deactivateAuthToken(String authToken) {
        try {
            authDAO.deleteAuth(authToken);
            return new DeactivationResult(true);
        } catch (DataAccessException e) {
            return new DeactivationResult(false, "Error: " + e.getMessage());
        }
    }
}
