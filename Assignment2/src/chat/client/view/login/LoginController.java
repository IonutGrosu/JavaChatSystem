package chat.client.view.login;

import chat.client.core.ViewHandler;
import chat.client.core.ViewModelFactory;
import chat.client.view.ViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController implements ViewController
{
  private ViewHandler vh;
  private LoginViewModel vm;

  @FXML private TextField usernameTextField;

  @Override public void init(ViewHandler vh, ViewModelFactory vmf)
  {
    this.vh = vh;
    this.vm = vmf.getLoginVM();

  }

  public void onLoginButton(ActionEvent actionEvent)
  {
    vh.openChatsView();
    vm.startClient(usernameTextField.textProperty().getValue());
  }
}
