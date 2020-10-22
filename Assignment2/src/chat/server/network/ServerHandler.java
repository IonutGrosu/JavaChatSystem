package chat.server.network;

import chat.server.model.ServerModelInterface;
import chat.shared.transferObjects.Message;
import chat.shared.transferObjects.Request;
import chat.shared.transferObjects.RequestType;
import chat.shared.transferObjects.User;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandler implements Runnable {
    private Socket socket;
    private ServerModelInterface model;

    private String clientUsername;

    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;

    public ServerHandler(Socket socket, ServerModelInterface model) {
        this.socket = socket;
        this.model = model;

        try {
            inFromClient = new ObjectInputStream(socket.getInputStream());
            outToClient = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.addListener(RequestType.SUCCESSFUL_LOGIN.toString(), this::writeToClient);
        model.addListener(RequestType.EXISTING_USERNAME.toString(), this::writeToClient);
        model.addListener(RequestType.UPDATE_ACTIVE_USERS.toString(), this::writeToClient);
        model.addListener(RequestType.GET_ACTIVE_USERS.toString(), this::writeToClient);
        model.addListener(RequestType.RECEIVE_PUBLIC.toString(), this::writeToClient);
    }

    private void writeToClient(PropertyChangeEvent event) {
        if (!socket.isClosed()) {
            try {
                Request requestToClient = (Request) event.getNewValue();
                switch (requestToClient.getType()) {
                    case SUCCESSFUL_LOGIN: {
                        User userToSendTo = (User) requestToClient.getArg();
                        if (clientUsername != null && clientUsername.equals(userToSendTo.getUsername())) {
                            outToClient.writeObject(requestToClient);
                            System.out.println("Request to client(" + clientUsername + "): " + requestToClient.getType());
                        }
                        break;
                    }
                    case EXISTING_USERNAME: {
                        User userToSendTo = (User) requestToClient.getArg();
                        if (clientUsername != null && clientUsername.equals(userToSendTo.getUsername())) {
                            outToClient.writeObject(requestToClient);
                            System.out.println("Request to client(" + clientUsername + "): " + requestToClient.getType());
                        }
                        clientUsername = "";
                        break;
                    }
                    case UPDATE_ACTIVE_USERS:
                    case RECEIVE_PUBLIC: {
                        outToClient.writeObject(requestToClient);
                        System.out.println("Request to all clients: " + requestToClient.getType());
                        break;
                    }
                    case GET_ACTIVE_USERS: {
                        String clientToSendTo = (String) event.getOldValue();
                        if (clientToSendTo.equals(clientUsername)) {
                            outToClient.writeObject(requestToClient);
                            System.out.println("Request to client(" + clientUsername + "): " + requestToClient.getType());
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Request clientRequest = (Request) inFromClient.readUnshared();
                System.out.println("< Request from client: " + clientRequest.getType());

                if (!clientRequest.getType().equals(RequestType.DISCONNECT)) {
                    switch (clientRequest.getType()) {
                        case LOGIN: {
                            User clientUser = (User) clientRequest.getArg();
                            clientUsername = clientUser.getUsername();
                            model.loginUser(clientUser);
                            break;
                        }
                        case GET_ACTIVE_USERS: {
                            model.sendActiveUsersToClient((String) clientRequest.getArg());
                            break;
                        }
                        case SEND_PUBLIC: {
                            model.sendPublicMessage((Message) clientRequest.getArg());
                            break;
                        }
                        default:
                            System.out.println("Request: " + clientRequest + " could not be handled.");
                    }

                } else {
                    User userDisconnecting = (User) clientRequest.getArg();

                    model.removeListener(RequestType.SUCCESSFUL_LOGIN.toString(), this::writeToClient);
                    model.removeListener(RequestType.EXISTING_USERNAME.toString(), this::writeToClient);
                    model.removeListener(RequestType.UPDATE_ACTIVE_USERS.toString(), this::writeToClient);
                    model.removeListener(RequestType.GET_ACTIVE_USERS.toString(), this::writeToClient);
                    model.removeListener(RequestType.RECEIVE_PUBLIC.toString(), this::writeToClient);

                    socket.close();

                    model.disconnect(userDisconnecting);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
