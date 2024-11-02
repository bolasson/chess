package passoff.dataaccess;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MemoryUserDAOTest {
    private MemoryUserDAO userDAO;

    @BeforeEach
    public void setUp() {
        userDAO = new MemoryUserDAO();
    }

    @Test
    public void testClearUsers() throws DataAccessException {
        UserData user1 = new UserData("user1", "password1", "email1@example.com");
        UserData user2 = new UserData("user2", "password2", "email2@example.com");
        userDAO.createUser(user1);
        userDAO.createUser(user2);
        userDAO.clear();
        assertThrows(DataAccessException.class, () -> userDAO.getUser("user1"));
        assertThrows(DataAccessException.class, () -> userDAO.getUser("user2"));
    }

    @Test
    public void testCreateUser() throws DataAccessException {
        UserData user = new UserData("testUser", "password", "email@example.com");
        userDAO.createUser(user);
        assertEquals(user, userDAO.getUser("testUser"));
    }

    @Test
    public void testCreateDuplicateUser() {
        UserData user = new UserData("testUser", "password", "email@example.com");
        assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(user);
            userDAO.createUser(user);
        });
    }

    @Test
    public void testGetUser() throws DataAccessException {
        UserData user = new UserData("testUser", "password", "email@example.com");
        userDAO.createUser(user);
        UserData retrievedUser = userDAO.getUser("testUser");
        assertEquals("testUser", retrievedUser.username());
        assertEquals("password", retrievedUser.password());
        assertEquals("email@example.com", retrievedUser.email());
    }

    @Test
    public void testGetNonExistentUser() {
        assertThrows(DataAccessException.class, () -> userDAO.getUser("nonExistentUser"));
    }

    @Test
    public void testUpdateUser() throws DataAccessException {
        UserData user = new UserData("testUser", "password", "email@example.com");
        userDAO.createUser(user);
        UserData updatedUser = new UserData("testUser", "newPassword", "newemail@example.com");
        userDAO.updateUser(updatedUser);
        UserData result = userDAO.getUser("testUser");
        assertEquals("newPassword", result.password());
        assertEquals("newemail@example.com", result.email());
    }

    @Test
    public void testDeleteUser() throws DataAccessException {
        UserData user = new UserData("testUser", "password", "email@example.com");
        userDAO.createUser(user);
        userDAO.deleteUser("testUser");
        assertThrows(DataAccessException.class, () -> userDAO.getUser("testUser"));
    }

    @Test
    public void testUserExists() throws DataAccessException {
        UserData user = new UserData("testUser", "password", "email@example.com");
        userDAO.createUser(user);
        assertTrue(userDAO.userExists("testUser"));
        assertFalse(userDAO.userExists("nonExistentUser"));
    }
}
