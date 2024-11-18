package results;

public record LogoutResult(boolean success, String message) {
    public LogoutResult(boolean success) {
        this(success, null);
    }
}