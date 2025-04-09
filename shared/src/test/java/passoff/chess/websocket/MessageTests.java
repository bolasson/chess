package passoff.chess.websocket;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import chess.*;
import websocket.messages.*;
import websocket.commands.UserGameCommand;

public class MessageTests {

    @Test
    public void userGameCommandSerialization() {
        ChessPosition start = new ChessPosition(2, 2);  // e.g., B2
        ChessPosition end = new ChessPosition(3, 3);    // e.g., C3
        ChessMove move = new ChessMove(start, end, null);
        UserGameCommand originalCommand = new UserGameCommand(
                UserGameCommand.CommandType.MAKE_MOVE,
                "test-token",
                1,
                move
        );
        Gson gson = new Gson();
        String json = gson.toJson(originalCommand);
        UserGameCommand deserializedCommand = gson.fromJson(json, UserGameCommand.class);
        assertEquals(originalCommand.getCommandType(), deserializedCommand.getCommandType());
        assertEquals(originalCommand.getAuthToken(), deserializedCommand.getAuthToken());
        assertEquals(originalCommand.getGameID(), deserializedCommand.getGameID());
        assertNotNull(originalCommand.getMove());
        assertNotNull(deserializedCommand.getMove());
        assertEquals(originalCommand.getMove(), deserializedCommand.getMove());
    }

    @Test
    public void errorMessageSerialization() {
        String errorText = "An error occurred.";
        ErrorMessage original = new ErrorMessage(errorText);
        Gson gson = new Gson();
        String json = gson.toJson(original);
        ErrorMessage deserialized = gson.fromJson(json, ErrorMessage.class);
        assertEquals(original.getServerMessageType(), deserialized.getServerMessageType());
        assertEquals(errorText, deserialized.getErrorMessage());
    }

    @Test
    public void notificationMessageSerialization() {
        String noteText = "User1 has connected.";
        NotificationMessage original = new NotificationMessage(noteText);
        Gson gson = new Gson();
        String json = gson.toJson(original);
        NotificationMessage deserialized = gson.fromJson(json, NotificationMessage.class);
        assertEquals(original.getServerMessageType(), deserialized.getServerMessageType());
        assertEquals(noteText, deserialized.getMessage());
    }

    @Test
    public void loadGameMessageSerialization() {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        ChessGame game = new ChessGame(board, ChessGame.TeamColor.WHITE);
        LoadGameMessage original = new LoadGameMessage(game);
        Gson gson = new Gson();
        String json = gson.toJson(original);
        LoadGameMessage deserialized = gson.fromJson(json, LoadGameMessage.class);
        assertEquals(original.getServerMessageType(), deserialized.getServerMessageType());
        assertEquals(game.getTeamTurn(), deserialized.getGame().getTeamTurn());
    }
}