import server.Server;

public class MainServer {
    public static void main(String[] args) {
        var server = new Server();
        server.run(8080);
    }
}