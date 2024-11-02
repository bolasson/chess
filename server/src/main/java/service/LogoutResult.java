package service;

public record LogoutResult(boolean success, String message) {
    public LogoutResult(boolean success) {
        this(success, null);
    }
}