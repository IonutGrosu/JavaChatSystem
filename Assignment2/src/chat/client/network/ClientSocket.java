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
import java.util.ArrayList;

public class ClientSocket implements ClientInterface{

    private final PropertyChangeSupport support;

    Socket socket;

    ObjectOutputStream outToServer;

    private String username;
    private Boolean socketClosed;

    public ClientSocket(){
        support = new PropertyChangeSupport(this);
    }

    public void startClient(String username)
    {
        this.username = username;
        support.firePropertyChange(RequestType.NEW_USER.toString(), null, username);
        try {
            socket = new Socket("localhost", 12345);
            socketClosed = false;

            outToServer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());

            new Thread(() -> listenToServer(outToServer, inFromServer)).start();

            outToServer.writeObject(new Request(RequestType.NEW_USER, this.username));
            System.out.println(this.username);

        } catch (IOException e) {
            System.out.println("Connection to server was interrupted..");
        }
    }

    private void listenToServer(ObjectOutputStream outToServer, ObjectInputStream inFromServer) {
        try {
            while (!socketClosed){
                Request requestFromServer = (Request)inFromServer.readObject();

                if (requestFromServer.getType().equals(RequestType.UPDATE_ACTIVE_USERS)){
                    ArrayList<String> list =(ArrayList<String>) requestFromServer.getArg();
                    System.out.println("user list from server: "+list.toString());
                }

                support.firePropertyChange(requestFromServer.getType().toString(), null, requestFromServer.getArg());

            }
        } catch (IOException | ClassNotFoundException e) {
            //e.printStackTrace();
        }
    }

    @Override
    public void sendMessageToAll(String message) {
        Message m = new Message(message,username);
        Request sendMessageRequest = new Request(RequestType.SEND_PUBLIC, m);
        try {
            outToServer.writeObject(sendMessageRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnectUser() {
        Request disconnectUserRequest = new Request(RequestType.DISCONNECT, username);
        try {
            outToServer.writeObject(disconnectUserRequest);
            socket.close();
            socketClosed = true;
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
