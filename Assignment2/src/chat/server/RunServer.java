package chat.server;

import chat.server.model.ServerModel;
import chat.server.network.Server;

public class RunServer {
    public static void main(String[] args) {
        Server server = new Server(new ServerModel());
        server.start();
    }
}
