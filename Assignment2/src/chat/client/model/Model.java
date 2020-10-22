package chat.client.model;

import chat.client.network.ClientInterface;
import chat.shared.transferObjects.Message;
import chat.shared.transferObjects.RequestType;
import chat.shared.transferObjects.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class Model implements ModelInterface {
    private ClientInterface client;
    private PropertyChangeSupport support;

    private ArrayList<String> avatarImages;

    private User user;


    public Model(ClientInterface client) {
        support = new PropertyChangeSupport(this);

        this.client = client;
        client.startClient();

        avatarImages = new ArrayList<>();
        loadAvatarImages();

        user = new User();
        user.setAvatarPath(avatarImages.get(0));

        client.addListener(RequestType.SUCCESSFUL_LOGIN.toString(), this::firePropertyForward);
        client.addListener(RequestType.EXISTING_USERNAME.toString(), this::firePropertyForward);
        client.addListener(RequestType.UPDATE_ACTIVE_USERS.toString(), this::firePropertyForward);
        client.addListener(RequestType.GET_ACTIVE_USERS.toString(), this::firePropertyForward);
        client.addListener(RequestType.RECEIVE_PUBLIC.toString(), this::firePropertyForward);
    }

    private void loadAvatarImages() {
        avatarImages.add("/chat/shared/pictures/female1.png");
        avatarImages.add("/chat/shared/pictures/female2.png");
        avatarImages.add("/chat/shared/pictures/female3.png");
        avatarImages.add("/chat/shared/pictures/female4.png");
        avatarImages.add("/chat/shared/pictures/female5.png");
        avatarImages.add("/chat/shared/pictures/male1.png");
        avatarImages.add("/chat/shared/pictures/male2.png");
        avatarImages.add("/chat/shared/pictures/male3.png");
        avatarImages.add("/chat/shared/pictures/male4.png");
        avatarImages.add("/chat/shared/pictures/previousArrow.png");
        avatarImages.add("/chat/shared/pictures/nextArrow.png");
    }

    private void firePropertyForward(PropertyChangeEvent propertyChangeEvent) {

        if (propertyChangeEvent.getPropertyName().equals(RequestType.SUCCESSFUL_LOGIN.toString())) {
            User user = (User) propertyChangeEvent.getNewValue();
            this.user.setUsername(user.getUsername());
        }

        support.firePropertyChange(propertyChangeEvent);
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
    public void login(String username) {
        user.setUsername(username);
        client.login(user);
    }

    @Override
    public void getActiveUsersList() {
        client.getActiveUsersList(user);
    }

    @Override
    public void sendPublicMessage(String message) {
        Message messageToSend = new Message(message, user);
        client.sendPublicMessage(messageToSend);
    }

    @Override
    public void getUsername() {
        support.firePropertyChange("USERNAME", null, user.getUsername());
    }

    @Override
    public void getFirstImages() {
        ArrayList<String> firstImages = new ArrayList<>();
        firstImages.add(avatarImages.get(0));//female1 image
        firstImages.add(avatarImages.get(avatarImages.size() - 1));//nextArrow image
        firstImages.add(avatarImages.get(avatarImages.size() - 2));//previousArrow image
        support.firePropertyChange(RequestType.AVATAR_IMAGES.toString(), null, firstImages);
    }

    @Override
    public void getNextAvatarImage(int currentIndex) {
        String nextAvatarImage;
        int nextIndex;
        if (currentIndex == avatarImages.size() - 3) {
            nextIndex = 0;
        } else {
            nextIndex = currentIndex + 1;
        }
        nextAvatarImage = avatarImages.get(nextIndex);
        user.setAvatarPath(nextAvatarImage);
        support.firePropertyChange(RequestType.NEXT_AVATAR_IMAGE.toString(), nextIndex, nextAvatarImage);
    }

    @Override
    public void getPreviousAvatarImage(int currentIndex) {
        String previousAvatarImage;
        int previousIndex;

        if (currentIndex == 0){
            previousIndex = avatarImages.size() - 3;
        } else {
            previousIndex = currentIndex - 1;
        }
        previousAvatarImage = avatarImages.get(previousIndex);
        user.setAvatarPath(previousAvatarImage);
        support.firePropertyChange(RequestType.PREVIOUS_AVATAR_IMAGE.toString(), previousIndex, previousAvatarImage);
    }

    @Override
    public void disconnect() {
        client.disconnect(user);
    }
}
