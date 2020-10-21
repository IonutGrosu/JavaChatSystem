package chat.server.model;

import chat.shared.transferObjects.Message;
import chat.shared.transferObjects.Request;
import chat.shared.transferObjects.RequestType;
import chat.shared.transferObjects.User;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class ServerModel implements ServerModelInterface {

    private PropertyChangeSupport support;

    private ArrayList<User> connectedUsers;

    public ServerModel()
    {
        support = new PropertyChangeSupport(this);

        connectedUsers = new ArrayList<>();
    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    @Override
    public void loginUser(User user) {
        Boolean existingUser = false;
        for (User i :connectedUsers) {
            if (i.getUsername().equals(user.getUsername())) existingUser = true;
        }

        if (!existingUser){
            connectedUsers.add(user);
            System.out.println(user.getUsername() + " logged in successfully");

            Request loginResultRequest = new Request(RequestType.SUCCESSFUL_LOGIN, user);
            support.firePropertyChange(RequestType.SUCCESSFUL_LOGIN.toString(), null, loginResultRequest);

            updateActiveUsers();
        }else{
            System.out.println("login attempt blocked ( " + user.getUsername() + " )" );

            Request loginResultRequest = new Request(RequestType.EXISTING_USERNAME, user);
            support.firePropertyChange(RequestType.EXISTING_USERNAME.toString(), null, loginResultRequest);
        }
    }

    @Override
    public void sendActiveUsersToClient(String username) {
        ArrayList<String> activeUsernames = new ArrayList<>();
        System.out.println("******" + username);

        for (User user :connectedUsers) {
            activeUsernames.add(user.getUsername());
        }

        Request updateActiveUsersRequest = new Request(RequestType.GET_ACTIVE_USERS, activeUsernames);
        support.firePropertyChange(updateActiveUsersRequest.getType().toString(), username, updateActiveUsersRequest);
    }

    @Override
    public void sendPublicMessage(Message messageToReceive) {
        Request sendPublicMessageRequest = new Request(RequestType.RECEIVE_PUBLIC, messageToReceive);
        support.firePropertyChange(sendPublicMessageRequest.getType().toString(), null, sendPublicMessageRequest);
    }

    public void updateActiveUsers() {
        ArrayList<String> activeUsernames = new ArrayList<>();

        for (User user :connectedUsers) {
            activeUsernames.add(user.getUsername());
        }

        Request updateActiveUsersRequest = new Request(RequestType.UPDATE_ACTIVE_USERS, activeUsernames);
        support.firePropertyChange(updateActiveUsersRequest.getType().toString(), null, updateActiveUsersRequest);
    }
}
