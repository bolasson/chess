package results;

public record JoinGameResult(boolean success, String message, int statusCode) {
    public JoinGameResult(boolean success) {
        this(success, null, 200);
    }

    public JoinGameResult(boolean success, String message) {
        this(success, message, success ? 200 : 400);
    }

    public JoinGameResult(boolean success, String message, int statusCode) {
        this.success = success;
        this.message = message;
        this.statusCode = statusCode;
    }
}