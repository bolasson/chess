package results;

import model.GameData;

import java.util.List;

public record ListGamesResult(boolean success, List<GameData> games, String message) {
    public ListGamesResult(boolean success, List<GameData> games) {
        this(success, games, null);
    }

    public ListGamesResult(boolean success, String message) {
        this(success, null, message);
    }
}