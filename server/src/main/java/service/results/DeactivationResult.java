package service.results;

public record DeactivationResult(boolean success, String message) {
    public DeactivationResult(boolean success) {
        this(success, null);
    }
}
