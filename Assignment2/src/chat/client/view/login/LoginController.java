package chat.client.view.login;

import chat.client.core.ViewHandler;
import chat.client.core.ViewModelFactory;
import chat.client.view.ViewController;
import javafx.event.ActionEvent;

public class LoginController implements ViewController
{
  private ViewHandler vh;
  private ViewModelFactory vmf;

  @Override public void init(ViewHandler vh, ViewModelFactory vmf)
  {
    this.vh = vh;
    this.vmf = vmf;
  }

  public void onLoginButton(ActionEvent actionEvent)
  {
    vh.openChatsView();
  }
}
