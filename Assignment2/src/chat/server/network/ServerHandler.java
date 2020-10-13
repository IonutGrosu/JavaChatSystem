package chat.server.network;

import chat.server.model.ConnectionPool;
import chat.shared.transferObjects.Request;
import chat.shared.transferObjects.RequestType;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerHandler implements Runnable {
    private Socket socket;
    private ConnectionPool cp;

    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;

    private String username;
    private Boolean socketClosed;

    public ServerHandler(Socket socket, ConnectionPool cp)
    {
        this.socket = socket;
        socketClosed = false;
        this.cp = cp;

        cp.addListener(RequestType.SEND_PUBLIC.toString(), this::sendMessageToAll);
        cp.addListener(RequestType.UPDATE_ACTIVE_USERS.toString(), this::updateActiveUsers);
        cp.addListener(RequestType.CLOSE_SOCKET.toString(), this::closeSocket);

        try
        {
            inFromClient = new ObjectInputStream(socket.getInputStream());
            outToClient = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void closeSocket(PropertyChangeEvent propertyChangeEvent) {
        if (this.username.equals((String)propertyChangeEvent.getNewValue()))
        {
            try {
                socket.close();
                socketClosed = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateActiveUsers(PropertyChangeEvent propertyChangeEvent) {
        try {
            Request request = (Request) propertyChangeEvent.getNewValue();
            ArrayList<String> list = (ArrayList<String>) request.getArg();
            System.out.println("user list sent to the user: "+list.toString());
            outToClient.writeObject(propertyChangeEvent.getNewValue());
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private void sendMessageToAll(PropertyChangeEvent propertyChangeEvent) {
        try {
            Request request = (Request) propertyChangeEvent.getNewValue();
            outToClient.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void run()
    {
        try
        {
            while (!socketClosed) {
                Request clientRequest = (Request) inFromClient.readObject();
                switch (clientRequest.getType())
                {
                    case NEW_USER:
                        cp.addActiveUser((String)clientRequest.getArg());
                        this.username = (String)clientRequest.getArg();
                        break;
                    case DISCONNECT:
                        cp.removeActiveUser((String)clientRequest.getArg());
                        break;
                    case SEND_PRIVATE:
                        break;
                    case SEND_PUBLIC:
                        cp.sendToAll(clientRequest);
                        break;
                }
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
