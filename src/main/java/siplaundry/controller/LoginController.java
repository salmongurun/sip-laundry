package siplaundry.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import siplaundry.service.AuthService;
import siplaundry.view.AdminView;
import siplaundry.view.auth.verification.UsernameVerification;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button BtnLogin;
    @FXML
    private TextField TxtUserName, TxtPassword;
    @FXML
    private BorderPane shadowRoot;

    public void initialize() {
        shadowRoot.setVisible(false);
    }

    public void ButtonLoginAction(ActionEvent ex) throws IOException {
        Stage stage = (Stage) TxtUserName.getScene().getWindow();
        TrayNotification tray = new TrayNotification();
        
         if(!new AuthService().login(TxtUserName.getText(), TxtPassword.getText())){
            tray.setTray("sign in","username atau password yang anda masukkan salah", NotificationType.WARNING, AnimationType.POPUP);
            tray.showAndDismiss(Duration.millis(500));
             return;
         }

        tray.setTray("sign in","Berhasil Login", NotificationType.SUCCESS, AnimationType.POPUP);
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
    }

    @FXML
    void showVerification(MouseEvent event) {
        new UsernameVerification(shadowRoot);
    }

}
