package chat.client;

import chat.client.core.ClientFactory;
import chat.client.core.ModelFactory;
import chat.client.core.ViewHandler;
import chat.client.core.ViewModelFactory;
import chat.client.network.ClientInterface;
import javafx.application.Application;
import javafx.stage.Stage;

public class ChatClient extends Application
{
  @Override public void start(Stage primaryStage) throws Exception
  {
    ClientFactory clientFactory = new ClientFactory();
    ModelFactory modelFactory = new ModelFactory(clientFactory);
    ViewModelFactory viewModelFactory = new ViewModelFactory(modelFactory);
    ViewHandler viewHandler = new ViewHandler(viewModelFactory, primaryStage);

    viewHandler.start();
  }
}
