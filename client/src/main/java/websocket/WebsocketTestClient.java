package websocket;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

public class WebsocketTestClient implements ServerMessageObserver {

    private final WebsocketCommunicator communicator;
    private final Gson gson = new Gson();

    public WebsocketTestClient(String uri) throws Exception {
        communicator = new WebsocketCommunicator(uri, this);
    }

    @Override
    public void notify(ServerMessage message) {
        System.out.println("Client received message: " + gson.toJson(message));
    }

    public void sendTestMessage() throws Exception {
        UserGameCommand command = new UserGameCommand(
                UserGameCommand.CommandType.CONNECT,
                "testAuthToken",
                1,
                null
        );
        System.out.println("Client sending: " + gson.toJson(command));
        communicator.sendMessage(command);
    }

    public void close() throws Exception {
        communicator.close();
    }

    public static void main(String[] args) {
        try {
            WebsocketTestClient testClient = new WebsocketTestClient("ws://localhost:8080/ws");
            Thread.sleep(1000);
            testClient.sendTestMessage();
            Thread.sleep(3000);
            testClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}