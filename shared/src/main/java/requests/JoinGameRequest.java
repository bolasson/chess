package requests;

public record JoinGameRequest(String authToken, int gameID, String playerColor) {}