package service;

public record JoinGameResult(boolean success, String message) {
    public JoinGameResult(boolean success) {
        this(success, null);
    }
}