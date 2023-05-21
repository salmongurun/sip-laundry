package siplaundry.controller.auth;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import siplaundry.entity.UserEntity;
import siplaundry.util.NumberUtil;

public class CodeVerifyController {
    @FXML
    private HBox close_btn;

    @FXML
    private Text phone_number;

    private UserEntity user;

    public CodeVerifyController(UserEntity user) {
        this.user = user;
    }

    @FXML
    void initialize() {
        phone_number.setText("+" + NumberUtil.maskPhoneNumber(user.getPhone()));
    }

    @FXML
    void closeModal() {
        Window window = close_btn.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
