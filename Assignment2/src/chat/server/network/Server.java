package chat.server.network;

import chat.server.model.ConnectionPool;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ConnectionPool cp;

    public Server(ConnectionPool cp) {
        this.cp = cp;
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server started and listening on " + InetAddress.getLocalHost().getHostAddress());

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected.");

                ServerHandler csh = new ServerHandler(socket, cp);

                Thread t = new Thread(csh);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
