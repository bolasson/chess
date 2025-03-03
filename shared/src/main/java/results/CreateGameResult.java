package results;

public record CreateGameResult(boolean success, Integer gameID, String message) {
    public CreateGameResult(boolean success, int gameID) {
        this(success, gameID, null);
    }

    public CreateGameResult(boolean success, String message) {
        this(success, null, message);
    }
}