package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MemoryAuthDAOTest {
    private MemoryAuthDAO authDAO;

    @BeforeEach
    public void setUp() {
        authDAO = new MemoryAuthDAO();
    }

    @Test
    public void testClear() throws DataAccessException {
        AuthData auth1 = new AuthData("authToken1", "user1");
        AuthData auth2 = new AuthData("authToken2", "user2");
        authDAO.createAuth(auth1);
        authDAO.createAuth(auth2);
        authDAO.clear();
        assertThrows(DataAccessException.class, () -> authDAO.getAuth("authToken1"));
        assertThrows(DataAccessException.class, () -> authDAO.getAuth("authToken2"));
    }

    @Test
    public void testCreateAuth() throws DataAccessException {
        AuthData auth = new AuthData("authToken1", "user1");
        authDAO.createAuth(auth);
        assertEquals(auth, authDAO.getAuth("authToken1"));
    }

    @Test
    public void testDeleteAuth() throws DataAccessException {
        AuthData auth = new AuthData("authToken1", "user1");
        authDAO.createAuth(auth);
        authDAO.deleteAuth("authToken1");
        assertThrows(DataAccessException.class, () -> authDAO.getAuth("authToken1"));
    }

    @Test
    public void testAuthExists() throws DataAccessException {
        AuthData auth = new AuthData("authToken1", "user1");
        authDAO.createAuth(auth);
        assertTrue(authDAO.authExists("authToken1"));
        assertFalse(authDAO.authExists("nonExistentToken"));
    }
}
