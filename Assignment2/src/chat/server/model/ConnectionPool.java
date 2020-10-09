package chat.server.model;

import chat.shared.transferObjects.Request;
import chat.shared.util.Subject;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ConnectionPool implements Subject {

    private PropertyChangeSupport support;

    public ConnectionPool()
    {
        support = new PropertyChangeSupport(this);
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
        support.firePropertyChange(clientRequest.getType().toString(), null, clientRequest);
    }
}
