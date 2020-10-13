package chat.client.view.publicChat;

import chat.client.model.ModelInterface;
import chat.shared.transferObjects.Message;
import chat.shared.transferObjects.RequestType;
import chat.shared.util.Subject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class PublicChatViewModel implements Subject
{
  private ModelInterface model;

  private StringProperty sentMessageProperty;

  ArrayList<Label> activeUsersLabels;
  private String username;
  private String lastSender;

  private PropertyChangeSupport support;

  public PublicChatViewModel(ModelInterface model)
  {
    this.model = model;

    this.model.addListener(RequestType.SEND_PUBLIC.toString(), this::receivePublicMessage);
    this.model.addListener(RequestType.UPDATE_ACTIVE_USERS.toString(), this::updateActiveUsers);
    this.model.addListener(RequestType.NEW_USER.toString(), this::getUsername);

    sentMessageProperty = new SimpleStringProperty("");
    lastSender = "";

    support = new PropertyChangeSupport(this);
  }

  private void getUsername(PropertyChangeEvent propertyChangeEvent) {
    this.username = (String) propertyChangeEvent.getNewValue();
  }

  private void updateActiveUsers(PropertyChangeEvent propertyChangeEvent) {
    ArrayList<String> activeUsersList = (ArrayList<String>) propertyChangeEvent.getNewValue();

    activeUsersLabels = new ArrayList<>();
    for (String activeUser : activeUsersList) {
      Label activeUserToAdd = new Label(activeUser);
      activeUserToAdd.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: center;");
      activeUsersLabels.add(activeUserToAdd);
    }

    support.firePropertyChange(RequestType.UPDATE_ACTIVE_USERS.toString(), null, activeUsersLabels);
  }

  private HBox createMessageContainer(Message message){
    HBox hBoxContainer = new HBox();
    hBoxContainer.getStyleClass().add("hBoxMessageContainer");
    VBox vBoxContainer = new VBox();
    vBoxContainer.getStyleClass().add("vBoxMessageContainer");

    if (message.getSender().equals(username)) {
      hBoxContainer.getStyleClass().add("right");
      vBoxContainer.getStyleClass().add("right");
    }

    if (!message.getSender().equals(lastSender)){
      Label usernameLabel = new Label(message.getSender());
      usernameLabel.getStyleClass().add("messageUsernameLabel");
      vBoxContainer.getChildren().add(usernameLabel);
    }

    Label messageBodyLabel = new Label(message.getMessageBody());
    messageBodyLabel.getStyleClass().add("messageBodyLabel");
    messageBodyLabel.setWrapText(true);
    messageBodyLabel.setMaxWidth(300);
    vBoxContainer.getChildren().add(messageBodyLabel);
    hBoxContainer.getChildren().add(vBoxContainer);

    lastSender = message.getSender();

    return hBoxContainer;
  }

  private void receivePublicMessage(PropertyChangeEvent propertyChangeEvent) {
    Message receivedMessage = (Message) propertyChangeEvent.getNewValue();

    support.firePropertyChange(RequestType.RECEIVE_PUBLIC.toString(), null, createMessageContainer(receivedMessage));
  }

  public StringProperty getSentMessageProperty()
  {
    return sentMessageProperty;
  }

  public void sendMessage() {
    model.sendMessage(sentMessageProperty.getValue());
  }

  @Override
  public void addListener(String eventName, PropertyChangeListener listener) {
    support.addPropertyChangeListener(eventName, listener);
  }

  @Override
  public void removeListener(String eventName, PropertyChangeListener listener) {
    support.removePropertyChangeListener(eventName, listener);
  }

  public void disconnectUser() {
    model.disconnectUser();
  }
}
