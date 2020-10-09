package chat.server.network;

import chat.server.model.ConnectionPool;
import chat.shared.transferObjects.Message;
import chat.shared.transferObjects.Request;
import chat.shared.transferObjects.RequestType;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandler implements Runnable {
    private Socket socket;
    private ConnectionPool cp;

    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;

    private String userName;

    public ServerHandler(Socket socket, ConnectionPool cp)
    {
        this.socket = socket;
        this.cp = cp;
        cp.addListener(RequestType.SEND_ALL.toString(), this::sendMessageToAll);
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
            while (true) {
                Request clientRequest = (Request) inFromClient.readObject();
                System.out.println((Message)clientRequest.getArg());
                switch (clientRequest.getType())
                {
                    case SEND:
                        break;
                    case SEND_ALL:
                        cp.sendToAll(clientRequest);
                        break;
                    case RECEIVE:
                        break;
                    case RECEIVE_ALL:
                        break;
                }
            }


//            userName = (String) inFromClient.readObject();
//            System.out.println(userName);
//            while (true)
//            {
//                Message message = (Message) inFromClient.readObject();
//
//                String body = message.getMessageBody();
//                System.out.println(message);
//
//                if (body.equalsIgnoreCase("exit")) {
//                    cp.removeConnection(this);
//                    outToClient.writeObject(message);
//                    socket.close();
//                    break;
//                }
//
//                cp.broadcast(message);
//            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void sendMessageToClient(Message msg)
    {
        try
        {
            outToClient.writeObject(msg);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String getClientName()
    {
        return userName;
    }
}
