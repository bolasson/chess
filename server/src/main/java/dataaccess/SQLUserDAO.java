package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements IUserDAO {

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Users";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing Users table: " + e.getMessage());
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean userExists(String username) throws DataAccessException {
        String sql = "SELECT COUNT(*) FROM Users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new DataAccessException("Error checking for user: " + e.getMessage());
        }
    }
}
