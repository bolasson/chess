package results;

public record RegisterResult(boolean success, String authToken, String username, String message, int statusCode) {
    public RegisterResult(boolean success, String authToken, String username) {
        this(success, authToken, username, null, success ? 200 : 400);
    }

    public RegisterResult(boolean success, String message, int statusCode) {
        this(success, null, null, message, statusCode);
    }
}
