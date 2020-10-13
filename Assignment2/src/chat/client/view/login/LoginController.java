package chat.client.view.login;

import chat.client.core.ViewHandler;
import chat.client.core.ViewModelFactory;
import chat.client.view.ViewController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController implements ViewController
{
  private ViewHandler vh;
  private LoginViewModel vm;

  @FXML private TextField usernameTextField;

  @Override public void init(ViewHandler vh, ViewModelFactory vmf)
  {
    this.vh = vh;
    this.vm = vmf.getLoginVM();

    usernameTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
          onLoginButton();
      }
    });

  }

  public void onLoginButton()
  {
    vh.openPublicChatView();
    vm.startClient(usernameTextField.textProperty().getValue());
  }
}
