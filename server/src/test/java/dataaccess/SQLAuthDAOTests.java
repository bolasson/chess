package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthDAOTests {
    private SQLAuthDAO authDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        authDAO = new SQLAuthDAO();
        try {
            DatabaseManager.initializeDatabase();
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
        authDAO.clear();
    }

    @AfterEach
    public void clearData() throws DataAccessException {
        authDAO.clear();
    }

    @Test
    public void clearSuccess() throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Test
    public void createAuthSuccess() throws DataAccessException {
        AuthData auth = new AuthData("authToken1", "user1");
        try {
            authDAO.createAuth(auth);
            AuthData retrievedAuth = authDAO.getAuth("authToken1");
            assertNotNull(retrievedAuth);
            assertEquals("authToken1", retrievedAuth.authToken());
            assertEquals("user1", retrievedAuth.username());
        } catch (DataAccessException e) {
            System.err.println("Error creating auth token: " + e.getMessage());
        }
    }

    @Test
    public void createAuthFailureDuplicateToken() throws DataAccessException {
        AuthData auth = new AuthData("authToken1", "user1");
        try {
            authDAO.createAuth(auth);
            assertThrows(DataAccessException.class, () -> {
                authDAO.createAuth(auth);
            });
        } catch (DataAccessException e) {
            System.err.println("Error duplicating auth token: " + e.getMessage());
        }
    }

    @Test
    public void getAuthSuccess() throws DataAccessException {
        AuthData auth = new AuthData("authToken1", "user1");
        try {
            authDAO.createAuth(auth);
            AuthData retrievedAuth = authDAO.getAuth("authToken1");
            assertNotNull(retrievedAuth);
            assertEquals("authToken1", retrievedAuth.authToken());
            assertEquals("user1", retrievedAuth.username());
        } catch (DataAccessException e) {
            System.err.println("Error retrieving auth token: " + e.getMessage());
        }
    }

    @Test
    public void getAuthFailureDoesNotExist() throws DataAccessException {
        try {
            AuthData retrievedAuth = authDAO.getAuth("nonExistentToken");
            assertNull(retrievedAuth, "Expected null for a non-existent auth token.");
        } catch (DataAccessException e) {
            System.err.println("Error retrieving non-existent auth token: " + e.getMessage());
        }
    }

    @Test
    public void deleteAuthSuccess() throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Test
    public void deleteAuthFailureDoesNotExist() throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
