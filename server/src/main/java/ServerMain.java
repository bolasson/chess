import spark.Spark;
import websocket.GameWebsocketHandler;

public class ServerMain {
    public static void main(String[] args) {
        Spark.port(8080);
        Spark.webSocket("/ws", GameWebsocketHandler.class);
        Spark.init();
        System.out.println("WebSocket server started at ws://localhost:8080/ws");
    }
}