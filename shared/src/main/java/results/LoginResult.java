package results;

public record LoginResult(boolean success, String authToken, String username, String message, int statusCode) {
    public LoginResult(boolean success, String authToken, String username) {
        this(success, authToken, username, null, success ? 200 : 400);
    }

    public LoginResult(boolean success, String message, int statusCode) {
        this(success, null, null, message, statusCode);
    }
}