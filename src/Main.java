import network.Server;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting TeamFileSharing Server...");
        int port = 12345;
        Server server = new Server(port);
        server.start();
    }
}
