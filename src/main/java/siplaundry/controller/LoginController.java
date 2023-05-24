package siplaundry.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import siplaundry.service.AuthService;
import siplaundry.util.ViewUtil;
import siplaundry.view.auth.VerificationView;
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
    @FXML
    private HBox rfid_btn;

    public void initialize() {
        shadowRoot.setVisible(false);
    }

    public void ButtonLoginAction() throws IOException {
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
                new ViewUtil().authRedirector(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        timeline.play();
    }

    @FXML
    void showVerification(MouseEvent event) {
        new VerificationView(shadowRoot);
    }

    @FXML
    void showRfidPage() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/auth/login-rfid.fxml"));
        Stage stage = (Stage) rfid_btn.getScene().getWindow();

        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
        }catch (IOException e) { e.printStackTrace(); }
    }

}
