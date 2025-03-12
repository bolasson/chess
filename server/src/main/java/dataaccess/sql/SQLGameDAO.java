package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.IGameDAO;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLGameDAO implements IGameDAO {
    private final Gson gson = new Gson();

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Games";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing Games table: " + e.getMessage());
        }
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        String sql = "INSERT INTO Games (gameID, whiteUsername, blackUsername, gameName, gameState) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gameData.gameID());
            stmt.setString(2, gameData.whiteUsername());
            stmt.setString(3, gameData.blackUsername());
            stmt.setString(4, gameData.gameName());
            stmt.setString(5, gson.toJson(gameData.game()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error creating game: " + e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, gameState FROM Games WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gameID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new GameData(
                        rs.getInt("gameID"),
                        rs.getString("whiteUsername"),
                        rs.getString("blackUsername"),
                        rs.getString("gameName"),
                        gson.fromJson(rs.getString("gameState"), ChessGame.class)
                );
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving game: " + e.getMessage());
        }
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, gameState FROM Games";
        List<GameData> games = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                games.add(new GameData(
                        rs.getInt("gameID"),
                        rs.getString("whiteUsername"),
                        rs.getString("blackUsername"),
                        rs.getString("gameName"),
                        gson.fromJson(rs.getString("gameState"), ChessGame.class)
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing games: " + e.getMessage());
        }
        return games;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        String sql = "UPDATE Games SET whiteUsername = ?, blackUsername = ?, gameName = ?, gameState = ? WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, gameData.whiteUsername());
            stmt.setString(2, gameData.blackUsername());
            stmt.setString(3, gameData.gameName());
            stmt.setString(4, gson.toJson(gameData.game()));
            stmt.setInt(5, gameData.gameID());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DataAccessException("No game found with ID: " + gameData.gameID());
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error updating game: " + e.getMessage());
        }
    }
}