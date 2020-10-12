package chat.client.view.chats;

import chat.client.model.ModelInterface;
import chat.shared.transferObjects.Message;
import chat.shared.transferObjects.RequestType;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

public class ChatsViewModel
{
  private ModelInterface model;

  private StringProperty activeUsersProperty;
  private StringProperty sentMessageProperty;
  private StringProperty receivedMessageProperty;

  private String activeUsers;

  public ChatsViewModel(ModelInterface model)
  {
    this.model = model;

    this.model.addListener(RequestType.SEND_PUBLIC.toString(), this::getReceivedMessageProperty);
    this.model.addListener(RequestType.UPDATE_ACTIVE_USERS.toString(), this::updateActiveUsers);

    activeUsersProperty = new SimpleStringProperty("");
    sentMessageProperty = new SimpleStringProperty("");
    receivedMessageProperty = new SimpleStringProperty("");
  }

  private void updateActiveUsers(PropertyChangeEvent propertyChangeEvent) {
    ArrayList<String> activeUsersList = (ArrayList<String>) propertyChangeEvent.getNewValue();
    activeUsers = "";
    for (int i = 0; i< activeUsersList.size();i++) {
      if (i != activeUsersList.size()-1) activeUsers += activeUsersList.get(i) + ", ";
      else activeUsers += activeUsersList.get(i);
    }

    Platform.runLater(() -> {
      activeUsersProperty.setValue(activeUsers);
    });
  }

  private void getReceivedMessageProperty(PropertyChangeEvent propertyChangeEvent) {
    Message receivedMessage = (Message) propertyChangeEvent.getNewValue();
    receivedMessageProperty.setValue(receivedMessage.toString());
  }

  public StringProperty getSentMessageProperty()
  {
    return sentMessageProperty;
  }

  public StringProperty getReceivedMessageProperty()
  {
    return receivedMessageProperty;
  }

  public StringProperty getActiveUsersProperty(){
    return activeUsersProperty;
  }

  public void sendMessage() {
    model.sendMessage(sentMessageProperty.getValue());
  }
}
