import client.REPL;

public class Main {
    public static void main(String[] args) {
        String serverURL = "http://localhost:8080";
        if (args.length > 0) {
            serverURL = args[0];
        }
        new REPL(serverURL).run();
    }
}