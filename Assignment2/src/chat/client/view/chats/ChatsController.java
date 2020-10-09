package chat.client.view.chats;

import chat.client.core.ViewHandler;
import chat.client.core.ViewModelFactory;
import chat.client.view.ViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatsController implements ViewController
{
  @FXML private TextField messageInputField;
  @FXML private TextArea messagesTextArea;

  private ViewHandler vh;
  private ChatsViewModel vm;

  @Override public void init(ViewHandler vh, ViewModelFactory vmf)
  {
    this.vh = vh;
    this.vm = vmf.getChatsVM();

    messageInputField.textProperty().bindBidirectional(vm.getSentMessageProperty());
    messagesTextArea.textProperty().bind(vm.getReceivedMessageProperty());
  }

  public void onSendButton(ActionEvent actionEvent)
  {
    vm.sendMessage();
    messageInputField.textProperty().setValue("");
  }
}
