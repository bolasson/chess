package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

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
    public void clearSuccess() {
        UserData user = new UserData("testUser", "password123", "test@example.com");
        try {
            userDAO.createUser(user);
            assertTrue(userDAO.userExists("testUser"));
            userDAO.clear();
            assertFalse(userDAO.userExists("testUser"));
        } catch (DataAccessException e) {
            System.err.println("Error when clearing data: " + e.getMessage());
        }
    }

    @Test
    public void createUserSuccess() {
        UserData user = new UserData("testUser", "password123", "test@example.com");
        try {
            userDAO.createUser(user);
            UserData retrievedUser = userDAO.getUser("testUser");
            assertNotNull(retrievedUser);
            assertEquals("testUser", retrievedUser.username());
            assertEquals("test@example.com", retrievedUser.email());
        } catch (DataAccessException e) {
            System.err.println("Error when creating user: " + e.getMessage());
        }
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
    public void getUserSuccess() {
        UserData user = new UserData("testUser", "password123", "test@example.com");
        try {
            userDAO.createUser(user);

            UserData retrievedUser = userDAO.getUser("testUser");
            assertNotNull(retrievedUser);
            assertEquals("testUser", retrievedUser.username());
            assertEquals("test@example.com", retrievedUser.email());
        } catch (DataAccessException e) {
            fail("Exception thrown during testGetUserExists: " + e.getMessage());
        }
    }

    @Test
    public void getUserFailureDoesNotExist() {
        try {
            UserData retrievedUser = userDAO.getUser("nonExistentUser");
            assertNull(retrievedUser, "Expected null for a non-existent user.");
        } catch (DataAccessException e) {
            fail("Exception thrown during testGetUserNotExists: " + e.getMessage());
        }
    }

    @Test
    public void userExistsSuccess() {
        UserData user = new UserData("testUser", "password123", "test@example.com");
        try {
            assertFalse(userDAO.userExists("testUser"));
            userDAO.createUser(user);
            assertTrue(userDAO.userExists("testUser"));
        } catch (DataAccessException e) {
            System.err.println("Exception thrown during testUserExists: " + e.getMessage());
        }
    }

    @Test
    public void userExistsFailureUserDoesNotExist() {
        try {
            assertFalse(userDAO.userExists("nonExistentUser"));
        } catch (DataAccessException e) {
            System.err.println("Exception thrown during testUserExistsNotExists: " + e.getMessage());
        }
    }
}
