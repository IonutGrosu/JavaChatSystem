package chat.client.view.chats;

import chat.client.model.ModelInterface;
import chat.shared.transferObjects.Message;
import chat.shared.transferObjects.Request;
import chat.shared.transferObjects.RequestType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeEvent;

public class ChatsViewModel
{
  private ModelInterface model;

  private StringProperty sentMessageProperty;
  private StringProperty receivedMessageProperty;

  public ChatsViewModel(ModelInterface model)
  {
    this.model = model;
    this.model.addListener(RequestType.SEND_ALL.toString(), this::getReceivedMessageProperty);

    sentMessageProperty = new SimpleStringProperty();
    receivedMessageProperty = new SimpleStringProperty();
  }

  private void getReceivedMessageProperty(PropertyChangeEvent propertyChangeEvent) {
    Message receivedMessage = (Message) propertyChangeEvent.getNewValue();
    System.out.println(receivedMessage);
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

  public void sendMessage() {
    model.sendMessage(sentMessageProperty.getValue());
  }
}
