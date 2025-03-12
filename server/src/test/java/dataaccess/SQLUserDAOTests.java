package dataaccess;

import dataaccess.sql.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDAOTests {
    private SQLUserDAO userDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO = new SQLUserDAO();
        try {
            DatabaseManager.initializeDatabase();
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
        userDAO.clear();
    }

    @AfterEach
    public void clearData() throws DataAccessException {
        userDAO.clear();
    }

    @Test
    public void clearSuccess() throws DataAccessException {
        UserData user = new UserData("testUser", "password123", "test@example.com");
        userDAO.createUser(user);
        assertTrue(userDAO.userExists("testUser"));
        userDAO.clear();
        assertFalse(userDAO.userExists("testUser"));
    }

    @Test
    public void createUserSuccess() throws DataAccessException {
        UserData user = new UserData("testUser", "password123", "test@example.com");
        userDAO.createUser(user);
        UserData retrievedUser = userDAO.getUser("testUser");
        assertNotNull(retrievedUser);
        assertEquals("testUser", retrievedUser.username());
        assertEquals("test@example.com", retrievedUser.email());
    }

    @Test
    public void createUserFailureDuplicateUser() {
        UserData user = new UserData("testUser", "password123", "test@example.com");
        assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(user);
            userDAO.createUser(user);
        });
    }

    @Test
    public void getUserSuccess() throws DataAccessException {
        UserData user = new UserData("testUser2", "password321", "test@example.com");
        userDAO.createUser(user);
        UserData retrievedUser = userDAO.getUser("testUser2");
        assertNotNull(retrievedUser);
        assertEquals("testUser2", retrievedUser.username());
        assertEquals("test@example.com", retrievedUser.email());
    }

    @Test
    public void getUserFailureDoesNotExist() {
        assertThrows(DataAccessException.class, () -> {
            userDAO.getUser("nonExistentUser");
        }, "Expected DataAccessException for a non-existent user.");
    }

    @Test
    public void userExistsSuccess() throws DataAccessException {
        UserData user = new UserData("testUser", "password123", "test@example.com");
        assertFalse(userDAO.userExists("testUser"));
        userDAO.createUser(user);
        assertTrue(userDAO.userExists("testUser"));
    }

    @Test
    public void userExistsFailureUserDoesNotExist() throws DataAccessException {
        assertFalse(userDAO.userExists("nonExistentUser"));
    }
}
