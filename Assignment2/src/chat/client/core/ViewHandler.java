package chat.client.core;

import chat.client.view.chats.ChatsController;
import chat.client.view.login.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewHandler
{
  private ViewModelFactory vmf;

  private Stage primaryStage;
  private Scene loginScene;
  private Scene chatsScene;

  public ViewHandler(ViewModelFactory vmf, Stage primaryStage)
  {
    this.vmf = vmf;
    this.primaryStage = primaryStage;
    primaryStage.setResizable(false);
  }

  public void start()
  {
    openLoginView();
    primaryStage.show();
  }

  public void openLoginView()
  {
    FXMLLoader loader = new FXMLLoader();
    if (loginScene == null)
    {
      Parent root = getRootByPath("../view/login/LoginView.fxml", loader);
      LoginController controller = loader.getController();
      controller.init(this, vmf);
      loginScene = new Scene(root);
    }

    primaryStage.setTitle("Login");
    primaryStage.setScene(loginScene);
  }

  public void openChatsView()
  {
    FXMLLoader loader = new FXMLLoader();

    if (chatsScene == null)
    {
      Parent root = getRootByPath("../view/chats/ChatsView.fxml", loader);
      ChatsController controller = loader.getController();
      controller.init(this, vmf);
      chatsScene = new Scene(root);
    }
    primaryStage.setTitle("Chatting with friends");
    primaryStage.setScene(chatsScene);
  }

  private Parent getRootByPath(String path, FXMLLoader loader)
  {
    loader.setLocation(getClass().getResource(path));
    Parent root = null;
    try
    {
      root = loader.load();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return root;
  }

}
