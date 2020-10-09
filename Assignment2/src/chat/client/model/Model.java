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
        client.addListener(RequestType.SEND_ALL.toString(), this::receiveMessage);
    }

    private void receiveMessage(PropertyChangeEvent propertyChangeEvent) {
        support.firePropertyChange(propertyChangeEvent);
    }

    @Override
    public void setUsername(String username) {
        client.setUsername(username);
        System.out.println("username in model: " + username);
    }

    @Override
    public void startClient() {
        client.startClient();
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
