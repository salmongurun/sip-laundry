package siplaundry.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import siplaundry.service.AuthService;
import siplaundry.view.AdminView;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button BtnLogin;
    @FXML
    private TextField TxtUserName;
    @FXML
    private TextField TxtPassword;

    public void ButtonLoginAction(ActionEvent ex) throws IOException {
        String title = "sign in";
        String message1 = "username atau password yang anda masukkan salah";
        String message2 = "username atau password yang anda masukkan benar";
        Stage stage = (Stage) TxtUserName.getScene().getWindow();

        TrayNotification tray = new TrayNotification();
        AnimationType type = AnimationType.POPUP;
        
         if(!new AuthService().login(TxtUserName.getText(), TxtPassword.getText())){
             tray.setAnimationType(type);
             tray.setTitle(title);
             tray.setMessage(message1);

             tray.setNotificationType(NotificationType.WARNING);
             tray.showAndDismiss(Duration.millis(1000));
             return;
         }

        tray.setAnimationType(type);
        tray.setTitle(title);
        tray.setMessage(message2);

        tray.setNotificationType(NotificationType.SUCCESS);
        tray.showAndDismiss(Duration.millis(500));

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2500), ae -> {
            try {
                stage.setTitle("Administrator - SIP Laundry");
                (new AdminView()).start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        timeline.play();
        // (new AdminView()).start(stage);
    }

}
