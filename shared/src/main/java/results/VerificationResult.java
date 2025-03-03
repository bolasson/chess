package results;

public record VerificationResult(boolean success, String usernameOrMessage) {
    public VerificationResult(boolean success) {
        this(success, null);
    }
}