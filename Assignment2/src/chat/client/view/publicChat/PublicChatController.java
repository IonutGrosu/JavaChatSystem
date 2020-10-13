package chat.client.view.publicChat;

import chat.client.core.ViewHandler;
import chat.client.core.ViewModelFactory;
import chat.client.view.ViewController;
import chat.shared.transferObjects.RequestType;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;

public class PublicChatController implements ViewController
{
  @FXML private VBox activeUsersContainer;
  @FXML private VBox messagesContainer;
  @FXML private TextField messageInputField;

  private ViewHandler vh;
  private PublicChatViewModel vm;

  @Override public void init(ViewHandler vh, ViewModelFactory vmf)
  {
    this.vh = vh;
    this.vm = vmf.getChatsVM();

    messageInputField.textProperty().bindBidirectional(vm.getSentMessageProperty());

    messageInputField.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
          onSendButton();
      }
    });

    vm.addListener(RequestType.UPDATE_ACTIVE_USERS.toString(), this::updateActiveUsers);
    vm.addListener(RequestType.RECEIVE_PUBLIC.toString(), this::addReceivedMessage);
  }

  void playSound() {
    String path = "Assignment2/src/chat/shared/sounds/notification_sound_1.mp3";
    Media media = new Media(new File(path).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.play();
  }

  private void addReceivedMessage(PropertyChangeEvent propertyChangeEvent) {
    HBox receivedMessage = (HBox) propertyChangeEvent.getNewValue();

    playSound();

    Platform.runLater(() -> {
      messagesContainer.getChildren().add(receivedMessage);
    });

  }

  private void updateActiveUsers(PropertyChangeEvent propertyChangeEvent) {
    ArrayList<Label> activeUsers = (ArrayList<Label>) propertyChangeEvent.getNewValue();

    Platform.runLater(() -> {
      activeUsersContainer.getChildren().clear();
      activeUsersContainer.getChildren().addAll(activeUsers);
    });
  }

  public void onSendButton(){
    vm.sendMessage();
    messageInputField.textProperty().setValue("");
  }

  public void onNewChatButton() {
  }

  public void onDisconnectButton() {
    vm.disconnectUser();
    vh.openLoginView();
  }
}
