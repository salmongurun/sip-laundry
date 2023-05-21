package siplaundry.controller.auth;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class UsernameVerifyController {
    @FXML
    private FontIcon username_icon;

    @FXML
    private TextField username_input;

    @FXML
    private HBox continue_btn;

    @FXML
    private FontIcon continue_icon;

    @FXML
    private HBox close_btn;

    private UserEntity user;
    private UsersRepo userRepo = new UsersRepo();
    private Consumer<UserEntity> verifyCodeAction;

    public UsernameVerifyController(Consumer<UserEntity> verifyCodeAction) {
        this.verifyCodeAction = verifyCodeAction;
    }
    @FXML
    void initialize() {
        username_icon.setVisible(false);
        continue_btn.setDisable(true);
    }

    @FXML
    void checkUsername() {
        if(username_input.getText().equals("")) {
            username_icon.setVisible(false);
            continue_btn.setDisable(true);
            return;
        }

        username_icon.setVisible(true);
        List<UserEntity> users = userRepo.get(new HashMap<>() {{
            put("username", username_input.getText());
        }});

        if(users.size() < 1) {
            username_icon.setIconLiteral("bx-x");
            username_icon.getStyleClass().remove("username-success");
            continue_btn.setDisable(true);
            return;
        }

        username_icon.setIconLiteral("bx-check");
        username_icon.getStyleClass().add("username-success");
        continue_btn.setDisable(false);
        this.user = users.get(0);
    }

    @FXML
    void closeModal() {
        Window window = close_btn.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    void continueAction() {
        continue_icon.setIconLiteral("bx-loader-alt");

        RotateTransition rts = new RotateTransition(Duration.seconds(1.5), continue_icon);
        rts.setByAngle(360);
        rts.setCycleCount(Animation.INDEFINITE);
        rts.setInterpolator(Interpolator.LINEAR);
        rts.play();

        verifyCodeAction.accept(this.user);
    }
}
