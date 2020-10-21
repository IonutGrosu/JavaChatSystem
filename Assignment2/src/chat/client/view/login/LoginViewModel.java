package chat.client.view.login;

import chat.client.model.ModelInterface;
import chat.shared.transferObjects.RequestType;
import chat.shared.util.Subject;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LoginViewModel implements Subject {
    private ModelInterface model;

    private PropertyChangeSupport support;

    private StringProperty errorLabelProperty;

    int currentAvatarImageIndex = 0;

    public LoginViewModel(ModelInterface model) {
        support = new PropertyChangeSupport(this);

        this.model = model;

        errorLabelProperty = new SimpleStringProperty("");

        model.addListener(RequestType.SUCCESSFUL_LOGIN.toString(), this::firePropertyForward);
        model.addListener(RequestType.EXISTING_USERNAME.toString(), this::updateErrorLabel);
        model.addListener(RequestType.AVATAR_IMAGES.toString(), this::firePropertyForward);
        model.addListener(RequestType.PREVIOUS_AVATAR_IMAGE.toString(), this::updateAvatarImage);
        model.addListener(RequestType.NEXT_AVATAR_IMAGE.toString(), this::updateAvatarImage);
    }

    private void updateAvatarImage(PropertyChangeEvent event) {
        currentAvatarImageIndex = (int) event.getOldValue();
        System.out.println("sending from vm to controller: " + event.getPropertyName() + event.getNewValue());
        support.firePropertyChange(RequestType.UPDATE_AVATAR_IMAGE.toString(), null, event.getNewValue());
    }

    private void updateErrorLabel(PropertyChangeEvent event) {
        Platform.runLater(() -> {
            errorLabelProperty.setValue("Existing username. Choose another one.");
        });
    }

    public void login(String username) {
        model.login(username);
    }

    private void firePropertyForward(PropertyChangeEvent event) {
        System.out.println("firing forward: " + event.getPropertyName());
        support.firePropertyChange(event);
    }

    public StringProperty getErrorLabelProperty() {
        return errorLabelProperty;
    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    public void getFirstImages() {
        System.out.println("vm asking for first images");
        model.getFirstImages();
    }

    public void getPreviousAvatarImage() {
        model.getPreviousAvatarImage(currentAvatarImageIndex);
    }

    public void getNextAvatarImage() {
        model.getNextAvatarImage(currentAvatarImageIndex);
    }
}
