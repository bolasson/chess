package passoff.service;

import dataaccess.*;
import jdk.jfr.Description;
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
}
