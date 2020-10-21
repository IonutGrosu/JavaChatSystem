package chat.client.view.publicChat;

import chat.client.core.ViewHandler;
import chat.client.core.ViewModelFactory;
import chat.client.view.ViewController;
import chat.shared.transferObjects.RequestType;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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

public class PublicChatController implements ViewController {
    @FXML
    private ScrollPane messagesScrollPane;
    @FXML
    private VBox activeUsersContainer;
    @FXML
    private VBox messagesContainer;
    @FXML
    private TextField messageInputField;

    private ViewHandler vh;
    private PublicChatViewModel vm;

    @Override
    public void init(ViewHandler vh, ViewModelFactory vmf) {
        this.vh = vh;
        this.vm = vmf.getChatsVM();

        messageInputField.textProperty().bindBidirectional(vm.getSentMessageProperty());

        vm.addListener(RequestType.UPDATE_ACTIVE_USERS.toString(), this::updateActiveUsers);
        vm.addListener(RequestType.RECEIVE_PUBLIC.toString(), this::addReceivedPublicMessage);

        messageInputField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                    onSendButton();
            }
        });
        vm.getActiveUsersList();
    }

    private void addReceivedPublicMessage(PropertyChangeEvent event) {
        HBox receivedMessage = (HBox) event.getNewValue();

        playSound();

        Platform.runLater(() -> {
            messagesContainer.getChildren().add(receivedMessage);

            messagesContainer.heightProperty().addListener(
                    (observable) -> {
                        messagesScrollPane.setVvalue(1.0d);
                    }
            );
        });
    }

    private void updateActiveUsers(PropertyChangeEvent event) {
        ArrayList<Label> activeUsersLabels = (ArrayList<Label>) event.getNewValue();

        Platform.runLater(() -> {
            activeUsersContainer.getChildren().clear();
            activeUsersContainer.getChildren().addAll(activeUsersLabels);
        });
    }

    private void playSound() {
        String path = "Assignment2/src/chat/shared/sounds/notification_sound_1.mp3";
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public void onSendButton() {
        String imputedMessage = messageInputField.textProperty().getValue();

        if (!imputedMessage.isEmpty()) {
            vm.sendPublicMessage();
            messageInputField.textProperty().setValue("");
        }
    }
}
