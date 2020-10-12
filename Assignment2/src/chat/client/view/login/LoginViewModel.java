package chat.client.view.login;

import chat.client.model.ModelInterface;
import javafx.beans.property.StringProperty;

public class LoginViewModel
{
  private ModelInterface model;

  public LoginViewModel(ModelInterface model)
  {
    this.model = model;
  }

  public void startClient(String username) {
    model.startClient(username);
  }
}
