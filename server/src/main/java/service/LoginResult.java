package service;

public record LoginResult(boolean success, String authToken, String username) {
    public LoginResult(boolean success, String message) {
        this(success, null, message);
    }
}