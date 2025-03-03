package results;

public record GenerationResult(boolean success, String authToken) {
    public GenerationResult(boolean success) {
        this(success, null);
    }
}