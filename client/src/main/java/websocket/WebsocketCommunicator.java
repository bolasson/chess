package websocket;

import com.google.gson.Gson;
import javax.websocket.*;
import java.net.URI;
import websocket.messages.ServerMessage;

public class WebsocketCommunicator extends Endpoint {

    private Session session;
    private final ServerMessageObserver observer;
    private final Gson gson = new Gson();

    public WebsocketCommunicator(String uri, ServerMessageObserver observer) throws Exception {
        this.observer = observer;
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, new URI(uri));
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                observer.notify(serverMessage);
            }
        });
    }

    public void sendMessage(Object command) throws Exception {
        String json = gson.toJson(command);
        session.getBasicRemote().sendText(json);
    }

    public void close() throws Exception {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }
}