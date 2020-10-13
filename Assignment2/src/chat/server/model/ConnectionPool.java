package chat.server.model;

import chat.shared.transferObjects.Message;
import chat.shared.transferObjects.Request;
import chat.shared.transferObjects.RequestType;
import chat.shared.util.Subject;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class ConnectionPool implements Subject {

    private PropertyChangeSupport support;

    private ArrayList<String> activeUsers;

    public ConnectionPool()
    {
        support = new PropertyChangeSupport(this);
        activeUsers = new ArrayList<>();
    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    public void sendToAll(Request clientRequest) {
        Message messageToSend = (Message)clientRequest.getArg();
        if (!messageToSend.getMessageBody().equals(""))
        {
            support.firePropertyChange(clientRequest.getType().toString(), null, clientRequest);
        }
    }

    public void addActiveUser(String user)
    {
        activeUsers.add(user);
        Request updateUsersRequest = new Request(RequestType.UPDATE_ACTIVE_USERS, new ArrayList<>(activeUsers));
        support.firePropertyChange(updateUsersRequest.getType().toString(), null, updateUsersRequest);
    }

    public void removeActiveUser(String user)
    {
        activeUsers.remove(user);
        Request updateUsersRequest = new Request(RequestType.UPDATE_ACTIVE_USERS, new ArrayList<>(activeUsers));
        support.firePropertyChange(updateUsersRequest.getType().toString(), null, updateUsersRequest);
        support.firePropertyChange(RequestType.CLOSE_SOCKET.toString(), null ,user);
    }
}
