package dataaccess;

import dataaccess.sql.SQLAuthDAO;
import dataaccess.sql.SQLUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthDAOTests {
    private SQLAuthDAO authDAO;
    private SQLUserDAO userDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        authDAO = new SQLAuthDAO();
        userDAO = new SQLUserDAO();
        try {
            DatabaseManager.initializeDatabase();
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
        authDAO.clear();
        userDAO.clear();
    }

    @AfterEach
    public void clearData() throws DataAccessException {
        authDAO.clear();
        userDAO.clear();
    }

    @Test
    public void clearSuccess() throws DataAccessException {
        AuthData auth = new AuthData("authToken1", "user1");
        UserData user = new UserData("user1", "password123", "user1@example.com");
        userDAO.createUser(user);
        authDAO.createAuth(auth);
        assertNotNull(authDAO.getAuth("authToken1"));
        authDAO.clear();
        assertThrows(DataAccessException.class, () -> authDAO.getAuth("authToken1"));
    }


    @Test
    public void createAuthSuccess() throws DataAccessException {
        AuthData auth = new AuthData("authToken1", "user1");
        UserData user = new UserData("user1", "password123", "user1@example.com");
        userDAO.createUser(user);
        authDAO.createAuth(auth);
        AuthData retrievedAuth = authDAO.getAuth("authToken1");
        assertNotNull(retrievedAuth);
        assertEquals("authToken1", retrievedAuth.authToken());
        assertEquals("user1", retrievedAuth.username());
    }

    @Test
    public void createAuthFailureDuplicateToken() throws DataAccessException {
        AuthData auth = new AuthData("authToken1", "user1");
        UserData user = new UserData("user1", "password123", "user1@example.com");
        userDAO.createUser(user);
        authDAO.createAuth(auth);
        assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(auth);
        });
    }

    @Test
    public void getAuthSuccess() throws DataAccessException {
        AuthData auth = new AuthData("authToken2", "user2");
        UserData user = new UserData("user2", "password321", "user2@example.com");
        userDAO.createUser(user);
        authDAO.createAuth(auth);
        AuthData retrievedAuth = authDAO.getAuth("authToken2");
        assertNotNull(retrievedAuth);
        assertEquals("authToken2", retrievedAuth.authToken());
        assertEquals("user2", retrievedAuth.username());
    }

    @Test
    public void getAuthFailureDoesNotExist() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> {
            authDAO.getAuth("nonExistentToken");
        });
    }

    @Test
    public void deleteAuthSuccess() throws DataAccessException {
        AuthData auth = new AuthData("authToken5", "user1");
        UserData user = new UserData("user1", "password123", "user1@example.com");
        userDAO.createUser(user);
        authDAO.createAuth(auth);
        assertNotNull(authDAO.getAuth("authToken5"));
        authDAO.deleteAuth("authToken5");
        assertThrows(DataAccessException.class, () -> authDAO.getAuth("authToken5"));
    }


    @Test
    public void deleteAuthFailureDoesNotExist() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> {
            authDAO.deleteAuth("nonExistentAuth");
        });
    }
}
