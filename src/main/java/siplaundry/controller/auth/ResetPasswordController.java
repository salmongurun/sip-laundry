package siplaundry.controller.auth;

import jakarta.validation.ConstraintViolation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.util.Duration;
import siplaundry.data.SessionData;
import siplaundry.entity.PasswordEntity;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;
import siplaundry.util.ValidationUtil;
import siplaundry.util.ViewUtil;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ResetPasswordController {
    @FXML
    private PasswordField txt_password, txt_confirm_password;
    private UserEntity user;
    private Map<String, Node> fields;
    private UsersRepo userRepo = new UsersRepo();

    public ResetPasswordController(UserEntity user) {
        this.user = user;
    }

    @FXML
    void initialize() {
        this.fields = new HashMap<>() {{
            put("password", txt_password);
            put("confirm", txt_confirm_password);
        }};
    }

    @FXML
    void resetPassword() {
        TrayNotification tray = new TrayNotification();
        PasswordEntity password = new PasswordEntity(
            txt_password.getText(),
            txt_confirm_password.getText()
        );

        Set<ConstraintViolation<PasswordEntity>> vols = ValidationUtil.validate(password);
        ValidationUtil.renderErrors(vols, this.fields);

        tray.setTray("Reset Password","Password berhasil diubah", NotificationType.SUCCESS, AnimationType.POPUP);
        tray.showAndDismiss(Duration.millis(500));

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2500), ae -> {
            Stage stage = (Stage) txt_password.getScene().getWindow();
            SessionData.user = user;
            ViewUtil.authRedirector(stage);
        }));

        timeline.play();

        user.setPassword(password.getPassword());
        userRepo.Update(user);

    }
}