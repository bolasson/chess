package passoff.service;

import dataaccess.*;
import service.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTests {
    private AuthService authService;
    private IAuthDAO authDAO;

    @BeforeEach
    public void setUp() {
        authDAO = new MemoryAuthDAO();
        authService = new AuthService(authDAO);
    }

    @Test
    public void verifyAuthTokenSuccess() throws DataAccessException {
        AuthData auth = new AuthData("validToken", "user1");
        authDAO.createAuth(auth);

        VerificationResult result = authService.verifyAuthToken("validToken");

        assertTrue(result.success());
        assertEquals("user1", result.usernameOrMessage());
    }

    @Test
    public void verifyAuthTokenFailureInvalidToken() {
        VerificationResult result = authService.verifyAuthToken("invalidToken");

        assertFalse(result.success());
        assertEquals("Invalid auth token", result.usernameOrMessage());
    }

    @Test
    public void generateAuthTokenSuccess() throws DataAccessException {
        GenerationResult result = authService.generateNewAuthToken("newUser");
        assertTrue(result.success());
        assertNotNull(result.authToken());
        AuthData authData = authDAO.getAuth(result.authToken());
        assertEquals("newUser", authData.username());
    }

    @Test
    public void generateAuthTokenFailureExistingToken() throws DataAccessException {
        AuthData existingAuth = new AuthData("existingToken", "user1");
        authDAO.createAuth(existingAuth);
        GenerationResult result = authService.generateNewAuthToken("user1");
        assertTrue(result.success());
        assertNotNull(result.authToken());
        assertNotEquals("existingToken", result.authToken());
    }

    @Test
    public void deactivateAuthTokenSuccess() throws DataAccessException {
        AuthData auth = new AuthData("authTokenToInvalidate", "user1");
        authDAO.createAuth(auth);
        DeactivationResult result = authService.deactivateAuthToken("authTokenToInvalidate");
        assertTrue(result.success());
        assertThrows(DataAccessException.class, () -> authDAO.getAuth("authTokenToInvalidate"));
    }

    @Test
    public void deactivateAuthTokenFailureInvalidToken() {
        DeactivationResult result = authService.deactivateAuthToken("nonExistentToken");
        assertFalse(result.success());
        assertEquals("Error: Auth token not found", result.message());
    }
}
