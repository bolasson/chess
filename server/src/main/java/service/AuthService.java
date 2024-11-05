package service;

import dataaccess.IAuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

public class AuthService {
    private final IAuthDAO authDAO;

    public AuthService(IAuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public VerificationResult verifyAuthToken(String authToken) {
        try {
            AuthData authData = authDAO.getAuth(authToken);
            return new VerificationResult(true, authData.username());
        } catch (DataAccessException e) {
            return new VerificationResult(false, "Invalid auth token");
        }
    }

    public GenerationResult generateNewAuthToken(String username) {
        try {
            String newAuthToken = java.util.UUID.randomUUID().toString();
            AuthData authData = new AuthData(newAuthToken, username);
            authDAO.createAuth(authData);
            return new GenerationResult(true, newAuthToken);
        } catch (DataAccessException e) {
            return new GenerationResult(false, "Error: " + e.getMessage());
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
