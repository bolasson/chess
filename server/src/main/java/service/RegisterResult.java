package service;

public record RegisterResult(boolean success, String authToken, String username) {
    public RegisterResult(boolean success, String message) {
        this(success, null, message);
    }
}