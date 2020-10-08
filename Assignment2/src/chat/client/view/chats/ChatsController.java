package chat.client.view.chats;

import chat.client.core.ViewHandler;
import chat.client.core.ViewModelFactory;
import chat.client.view.ViewController;
import javafx.event.ActionEvent;

public class ChatsController implements ViewController
{
  private ViewHandler vh;
  private ViewModelFactory vmf;

  @Override public void init(ViewHandler vh, ViewModelFactory vmf)
  {
    this.vh = vh;
    this.vmf = vmf;
  }

  public void onSendButton(ActionEvent actionEvent)
  {
  }
}
