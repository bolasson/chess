import dataaccess.DatabaseManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnectionTests {

    @Test
    public void testDatabaseConnection() {
        try (Connection conn = DatabaseManager.getConnection()) {
            Assertions.assertNotNull(conn, "Failed to create database connection.");
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT 1");
                Assertions.assertTrue(rs.next(), "No results from test query.");
                Assertions.assertEquals(1, rs.getInt(1), "Unexpected result from test query.");
            }
        } catch (Exception e) {
            Assertions.fail("Error: " + e.getMessage());
        }
    }
}
