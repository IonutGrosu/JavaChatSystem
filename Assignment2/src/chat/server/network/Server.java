package chat.server.network;

import chat.server.model.ServerModelInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerModelInterface model;

    public Server(ServerModelInterface cp) {
        this.model = cp;
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(8546);
            System.out.println("Server started and listening on " + InetAddress.getLocalHost().getHostAddress());

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("\nClient connected.");

                ServerHandler csh = new ServerHandler(socket, model);

                Thread t = new Thread(csh);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
