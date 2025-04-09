package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.UserGameCommand;
import websocket.messages.*;
import chess.*;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class GameWebsocketHandler {

    private final Gson gson = new Gson();
    private static final Set<Session> sessions = ConcurrentHashMap.newKeySet();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        sessions.add(session);
        System.out.println("Server: Session connected: " + session.getRemoteAddress());
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.println("Server received: " + message);
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case UserGameCommand.CommandType.CONNECT:
                handleConnect(session, command);
                break;
            case UserGameCommand.CommandType.MAKE_MOVE:
                sendError(session, "MAKE_MOVE not implemented yet.");
                break;
            case UserGameCommand.CommandType.LEAVE:
                sendNotification("A player has left the game.");
                break;
            case UserGameCommand.CommandType.RESIGN:
                sendNotification("A player has resigned.");
                break;
            default:
                sendError(session, "Unknown command type.");
        }
    }

    private void handleConnect(Session session, UserGameCommand command) throws Exception {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        ChessGame game = new ChessGame(board, ChessGame.TeamColor.WHITE);
        LoadGameMessage loadMsg = new LoadGameMessage(game);
        String json = gson.toJson(loadMsg);
        session.getRemote().sendString(json);
        sendNotification("A new player has connected.");
    }

    private void sendNotification(String note) throws Exception {
        NotificationMessage notification = new NotificationMessage(note);
        String json = gson.toJson(notification);
        for (Session sesh : sessions) {
            if (sesh.isOpen()) {
                sesh.getRemote().sendString(json);
            }
        }
    }

    private void sendError(Session session, String errorMessage) throws Exception {
        NotificationMessage error = new NotificationMessage("Error: " + errorMessage);
        String json = gson.toJson(error);
        session.getRemote().sendString(json);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        sessions.remove(session);
        System.out.println("Server: Session closed: " + reason);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}
