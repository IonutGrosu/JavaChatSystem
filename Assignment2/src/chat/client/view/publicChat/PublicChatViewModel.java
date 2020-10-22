package chat.client.view.publicChat;

import chat.client.model.ModelInterface;
import chat.shared.transferObjects.Message;
import chat.shared.transferObjects.RequestType;
import chat.shared.transferObjects.User;
import chat.shared.util.Subject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    support = new PropertyChangeSupport(this);

    this.model = model;

    sentMessageProperty = new SimpleStringProperty("");
    lastSender = "";

    model.addListener("USERNAME", this::getClientUsername);
    model.addListener(RequestType.UPDATE_ACTIVE_USERS.toString(), this::updateActiveUsers);
    model.addListener(RequestType.GET_ACTIVE_USERS.toString(), this::updateActiveUsers);
    model.addListener(RequestType.RECEIVE_PUBLIC.toString(), this::receivePublicMessage);

    model.getUsername();
  }

  private void receivePublicMessage(PropertyChangeEvent event) {
    Message receivedMessage = (Message) event.getNewValue();

    support.firePropertyChange(RequestType.RECEIVE_PUBLIC.toString(), null, createMessageContainer(receivedMessage));
  }

  private void getClientUsername(PropertyChangeEvent event) {
    username = (String)event.getNewValue();
    //TODO check why i do not get to this event
  }

  private void updateActiveUsers(PropertyChangeEvent event) {
    ArrayList<String> receivedUserList = (ArrayList<String>) event.getNewValue();

    activeUsersLabels = new ArrayList<>();

    for (String s : receivedUserList) {
      Label activeUserToAdd = new Label(s);
      activeUserToAdd.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: center;");
      activeUsersLabels.add(activeUserToAdd);
    }
    support.firePropertyChange(RequestType.UPDATE_ACTIVE_USERS.toString(), null, activeUsersLabels);
  }

  private HBox createMessageContainer(Message message){
    User sender  = message.getSender();
    String avatarPath = sender.getAvatarPath();
    Image senderAvatarImg = new Image(avatarPath);
    ImageView avatarImageView = new ImageView(senderAvatarImg);
    avatarImageView.setFitWidth(20);
    avatarImageView.setPreserveRatio(true);

    HBox hBoxContainer = new HBox();
    hBoxContainer.getStyleClass().add("hBoxMessageContainer");
    VBox vBoxContainer = new VBox();
    vBoxContainer.getStyleClass().add("vBoxMessageContainer");
    HBox avatarPlusUsernameHBox = new HBox();

    if (message.getSenderUsername().equals(username)) {
      hBoxContainer.getStyleClass().add("right");
      vBoxContainer.getStyleClass().add("right");
      avatarPlusUsernameHBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
    }

    if (!message.getSenderUsername().equals(lastSender)){
      Label usernameLabel = new Label(message.getSenderUsername());
      usernameLabel.getStyleClass().add("messageUsernameLabel");
      avatarPlusUsernameHBox.getChildren().add(avatarImageView);
      avatarPlusUsernameHBox.getChildren().add(usernameLabel);
      vBoxContainer.getChildren().add(avatarPlusUsernameHBox);
    }

    Label messageBodyLabel = new Label(message.getMessageBody());
    messageBodyLabel.getStyleClass().add("messageBodyLabel");
    messageBodyLabel.setWrapText(true);
    messageBodyLabel.setMaxWidth(300);
    vBoxContainer.getChildren().add(messageBodyLabel);

    hBoxContainer.getChildren().add(vBoxContainer);

    lastSender = message.getSenderUsername();

    return hBoxContainer;
  }

  public StringProperty getSentMessageProperty()
  {
    return sentMessageProperty;
  }

  @Override
  public void addListener(String eventName, PropertyChangeListener listener) {
    support.addPropertyChangeListener(eventName, listener);
  }

  @Override
  public void removeListener(String eventName, PropertyChangeListener listener) {
    support.removePropertyChangeListener(eventName, listener);
  }

  public void getActiveUsersList() {
    model.getActiveUsersList();
  }

  public void sendPublicMessage() {
    model.sendPublicMessage(sentMessageProperty.getValue());
  }

  public void disconnect() {
    model.disconnect();
  }
}
