package chat.client.model;

import chat.client.network.ClientInterface;
import chat.shared.transferObjects.RequestType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Model implements ModelInterface
{
    private ClientInterface client;
    private PropertyChangeSupport support;

    public Model(ClientInterface client) {
        support = new PropertyChangeSupport(this);

        this.client = client;
        client.addListener(RequestType.SEND_PUBLIC.toString(), this::receiveMessage);
        client.addListener(RequestType.UPDATE_ACTIVE_USERS.toString(), this::updateActiveUsers);
    }

    private void updateActiveUsers(PropertyChangeEvent propertyChangeEvent) {
        support.firePropertyChange(propertyChangeEvent);
    }

    private void receiveMessage(PropertyChangeEvent propertyChangeEvent) {
        support.firePropertyChange(propertyChangeEvent);
    }

    @Override
    public void startClient(String username) {
        client.startClient(username);
    }

    @Override
    public void sendMessage(String message) {
        client.sendMessageToAll(message);
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
