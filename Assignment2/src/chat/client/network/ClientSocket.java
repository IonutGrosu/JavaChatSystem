package chat.client.network;

import chat.shared.transferObjects.Message;
import chat.shared.transferObjects.Request;
import chat.shared.transferObjects.RequestType;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket implements ClientInterface{

    private PropertyChangeSupport support;

    ObjectOutputStream outToServer;

    private String username;

    public ClientSocket(){
        support = new PropertyChangeSupport(this);
    }

    public void startClient()
    {
        try {
            Socket socket = new Socket("localhost", 12345);

            outToServer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());

            new Thread(() -> listenToServer(outToServer, inFromServer)).start();

            //outToServer.writeObject(username);
            System.out.println(username);

        } catch (IOException e) {
            System.out.println("Connection to server was interrupted..");
        }


    }

    private void listenToServer(ObjectOutputStream outToServer, ObjectInputStream inFromServer) {
        try {
            while (true){
                Request request = (Request)inFromServer.readObject();

                System.out.println((Message)request.getArg());
                support.firePropertyChange(request.getType().toString(), null, request.getArg());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
        System.out.println("username in client: " + username);
    }

    @Override
    public void sendMessageToAll(String message) {
        Message m = new Message(message,username);
        Request sendMessageRequest = new Request(RequestType.SEND_ALL, m);
        try {
            outToServer.writeObject(sendMessageRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }
}
