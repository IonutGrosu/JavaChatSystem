package chat.server;

import chat.server.model.ConnectionPool;
import chat.server.network.Server;

public class StartServer {
    public static void main(String[] args) {
        Server server = new Server(new ConnectionPool());
        server.start();
    }
}
