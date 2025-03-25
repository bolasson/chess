package results;

public record CreateGameResult(boolean success, String gameName, Integer gameID, String message) {
    public CreateGameResult(boolean success, String gameName, int gameID) {
        this(success, gameName, gameID, null);
    }

    public CreateGameResult(boolean success, String gameName, String message) {
        this(success, gameName, null, message);
    }
}