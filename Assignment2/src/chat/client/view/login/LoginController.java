package chat.client.view.login;

import chat.client.core.ViewHandler;
import chat.client.core.ViewModelFactory;
import chat.client.view.ViewController;
import chat.shared.transferObjects.RequestType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.beans.PropertyChangeEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class LoginController implements ViewController {

    private ViewHandler vh;
    private LoginViewModel vm;

    @FXML
    private Label loginErrorLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Circle avatarContainer;
    @FXML
    private ImageView previousButton;
    @FXML
    private ImageView nextButton;

    String imputedUsername;

    @Override
    public void init(ViewHandler vh, ViewModelFactory vmf) {
        this.vh = vh;
        this.vm = vmf.getLoginVM();



        vm.addListener(RequestType.SUCCESSFUL_LOGIN.toString(), this::continueLogin);
        vm.addListener(RequestType.AVATAR_IMAGES.toString(), this::loadImages);
        vm.addListener("getFirstImages", this::loadImages);//TODO what is this?
        vm.addListener(RequestType.UPDATE_AVATAR_IMAGE.toString(), this::updateAvatarImage);

        loginErrorLabel.textProperty().bind(vm.getErrorLabelProperty());

        usernameTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                    onLoginButton();
            }
        });

        System.out.println("controller asking for first images");
        vm.getFirstImages();
    }

    private void updateAvatarImage(PropertyChangeEvent event) {
        String newImagePath = (String) event.getNewValue();
        System.out.println("***received new avatar image path: " + newImagePath);
        Image newImage = new Image(newImagePath);
        Platform.runLater(() -> {
            avatarContainer.setFill(new ImagePattern(newImage));

        });
    }

    private void loadImages(PropertyChangeEvent event) {
        ArrayList<String> receivedImages = (ArrayList<String>) event.getNewValue();
        System.out.println("controller received following images:" + receivedImages);
        Image avatarImg = new Image(receivedImages.get(0));
        avatarContainer.setFill(new ImagePattern(avatarImg));
        Image nextArrowImage = new Image(receivedImages.get(1));
        nextButton.setImage(nextArrowImage);
        Image previousArrowImage = new Image(receivedImages.get(2));
        previousButton.setImage(previousArrowImage);
    }

    private void continueLogin(PropertyChangeEvent event) {
        Platform.runLater(() -> {
            vh.openPublicChatView(imputedUsername);
        });

    }

    public void onLoginButton() {
        imputedUsername = usernameTextField.textProperty().getValue();
        //TODO check if text field is not empty
        vm.login(imputedUsername);
    }

    public void onPreviousImageButton(MouseEvent mouseEvent) {
        System.out.println("previous image..");
        vm.getPreviousAvatarImage();
    }

    public void onNextImageButton(MouseEvent mouseEvent) {
        System.out.println("next image..");
        vm.getNextAvatarImage();
    }

    public void loadImages(MouseEvent mouseEvent) {
        vm.getFirstImages();
    }//TODO do I have to delete the onClick event to load the first images?
}
