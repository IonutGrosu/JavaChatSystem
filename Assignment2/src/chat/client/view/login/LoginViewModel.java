package chat.client.view.login;

import chat.client.model.ModelInterface;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginViewModel
{
  private ModelInterface model;

  private StringProperty usernameProperty;

  public LoginViewModel(ModelInterface model)
  {
    this.model = model;
    usernameProperty = new SimpleStringProperty();
  }

  public void setUsername()
  {
    model.setUsername(usernameProperty.getValue());
    System.out.println("username in vm: " + usernameProperty.getValue());
  }

  public StringProperty getUsernameProperty() {
    return usernameProperty;
  }

  public void startClient() {
    model.startClient();
  }
}
