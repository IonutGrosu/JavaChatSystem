package chat.client.network;

import chat.shared.transferObjects.Message;
import chat.shared.transferObjects.Request;
import chat.shared.transferObjects.RequestType;
import chat.shared.transferObjects.User;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements ClientInterface {

    private final PropertyChangeSupport support;

    Socket socket;

    ObjectOutputStream outToServer;
    ObjectInputStream inFromServer;

    //private String clientUsername; delete this if everything works fine


    public Client() {
        support = new PropertyChangeSupport(this);
    }

    public void startClient() {
        try {
            socket = new Socket("localhost", 4567);

            outToServer = new ObjectOutputStream(socket.getOutputStream());
            inFromServer = new ObjectInputStream(socket.getInputStream());

            new Thread(() -> listenToServer(inFromServer)).start();


        } catch (IOException e) {
            System.out.println("Connection to server was interrupted..");
        }
    }

    @Override
    public void login(User user) {
        //clientUsername = user.getUsername(); delete this if everything works fine
        Request loginRequest = new Request(RequestType.LOGIN, user);
       writeToServer(loginRequest);
    }

    @Override
    public void getActiveUsersList(User user) {
        Request getActiveUsersRequest = new Request(RequestType.GET_ACTIVE_USERS, user.getUsername());
        writeToServer(getActiveUsersRequest);
    }

    @Override
    public void sendPublicMessage(Message messageToSend) {
        Request sendPublicMessageRequest = new Request(RequestType.SEND_PUBLIC, messageToSend);
        writeToServer(sendPublicMessageRequest);
    }

    @Override
    public void disconnect(User user) {
        Request disconnectRequest = new Request(RequestType.DISCONNECT, user);
        writeToServer(disconnectRequest);
    }

    private void listenToServer(ObjectInputStream inFromServer) {
        try {
            while (true) {
                Request requestFromServer = (Request) inFromServer.readObject();
                System.out.println("< Request from server: " + requestFromServer.getType());
                support.firePropertyChange(requestFromServer.getType().toString(), null, requestFromServer.getArg());
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writeToServer(Request requestToServer){
        try {
            System.out.println("> Request to server: " + requestToServer.toString());
            outToServer.writeObject(requestToServer);
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
