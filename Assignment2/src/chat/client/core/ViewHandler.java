package chat.client.core;

import chat.client.view.publicChat.PublicChatController;
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
  private Scene publicChatScene;

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
    loginScene.getStylesheets().add(getClass().getResource("../../shared/stylesheets/styles.css").toExternalForm());
    primaryStage.setTitle("Login");
    primaryStage.setScene(loginScene);
  }

  public void openPublicChatView()
  {
    FXMLLoader loader = new FXMLLoader();

    if (publicChatScene == null)
    {
      Parent root = getRootByPath("../view/publicChat/PublicChatView.fxml", loader);
      PublicChatController controller = loader.getController();
      controller.init(this, vmf);
      publicChatScene = new Scene(root);
    }
    publicChatScene.getStylesheets().add(getClass().getResource("../../shared/stylesheets/styles.css").toExternalForm());
    primaryStage.setTitle("Chatting with friends");
    primaryStage.setScene(publicChatScene);
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
