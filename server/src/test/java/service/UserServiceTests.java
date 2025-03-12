package service;

import dataaccess.*;
import dataaccess.Memory.MemoryAuthDAO;
import dataaccess.Memory.MemoryUserDAO;
import jdk.jfr.Description;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.LogoutResult;
import results.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    private UserService userService;
    private IUserDAO userDAO;
    private IAuthDAO authDAO;

    @BeforeEach
    public void setUp() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    public void clearSuccess() throws DataAccessException {
        UserData user1 = new UserData("user1", "password1", "email1@example.com");
        UserData user2 = new UserData("user2", "password2", "email2@example.com");
        userDAO.createUser(user1);
        userDAO.createUser(user2);
        userService.clear();
        assertThrows(DataAccessException.class, () -> userDAO.getUser("user2"));
        assertThrows(DataAccessException.class, () -> userDAO.getUser("user1"));
    }

    @Test
    public void registerSuccess() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("newUser", "password123", "email@example.com");
        RegisterResult result = userService.register(request);
        assertTrue(result.success());
        assertNotNull(result.authToken());
        assertEquals("newUser", result.username());
        UserData user = userDAO.getUser("newUser");
        assertEquals("newUser", user.username());
        assertTrue(BCrypt.checkpw("password123", user.password()));
        assertEquals("email@example.com", user.email());
        AuthData authData = authDAO.getAuth(result.authToken());
        assertEquals("newUser", authData.username());
    }

    @Test
    @Description("This test tries to create a new user, but fails because the user already existing")
    public void registerFailureUserAlreadyExists() throws DataAccessException {
        UserData existingUser = new UserData("existingUser", "password123", "email@example.com");
        userDAO.createUser(existingUser);
        RegisterRequest request = new RegisterRequest("existingUser", "password123", "email@example.com");
        RegisterResult result = userService.register(request);
        assertFalse(result.success());
        assertNull(result.authToken());
        assertNull(result.username());
    }

    @Test
    public void loginSuccess() throws DataAccessException {
        UserData user = new UserData("testUser", "password123", "email@example.com");
        userDAO.createUser(user);
        LoginRequest request = new LoginRequest("testUser", "password123");
        LoginResult result = userService.login(request);
        assertTrue(result.success());
        assertNotNull(result.authToken());
        assertEquals("testUser", result.username());
        AuthData authData = authDAO.getAuth(result.authToken());
        assertEquals("testUser", authData.username());
    }

    @Test
    public void loginFailureInvalidPassword() throws DataAccessException {
        UserData user = new UserData("testUser", "password123", "email@example.com");
        userDAO.createUser(user);
        LoginRequest request = new LoginRequest("testUser", "wrongPassword");
        LoginResult result = userService.login(request);
        assertFalse(result.success());
        assertNull(result.authToken());
        assertEquals("Error: credentials are invalid", result.message());
    }

    @Test
    public void loginFailureNonExistentUser() {
        LoginRequest request = new LoginRequest("nonExistentUser", "password123");
        LoginResult result = userService.login(request);
        assertFalse(result.success());
        assertNull(result.authToken());
        assertEquals("Error: user does not exist", result.message());
    }

    @Test
    public void logoutSuccess() throws DataAccessException {
        AuthData auth = new AuthData("authToken1", "testUser");
        authDAO.createAuth(auth);
        LogoutResult result = userService.logout("authToken1");
        assertTrue(result.success());
        assertThrows(DataAccessException.class, () -> authDAO.getAuth("authToken1"));
    }

    @Test
    public void logoutFailureInvalidAuthToken() {
        LogoutResult result = userService.logout("invalidAuthToken");
        assertFalse(result.success());
        assertEquals("Error: Auth token not found", result.message());
    }
}
