package chat.server.network;

import chat.server.model.ServerModel;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerModel cp;

    public Server(ServerModel cp) {
        this.cp = cp;
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(4567);
            System.out.println("Server started and listening on " + InetAddress.getLocalHost().getHostAddress());

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("\nClient connected.");

                ServerHandler csh = new ServerHandler(socket, cp);

                Thread t = new Thread(csh);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
