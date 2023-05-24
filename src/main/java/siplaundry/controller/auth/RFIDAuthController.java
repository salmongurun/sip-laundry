package siplaundry.controller.auth;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import siplaundry.entity.UserEntity;

public class RFIDAuthController {
    @FXML
    private TextField input_rfid;

    @FXML
    void initialize() {
        input_rfid.requestFocus();
        input_rfid.textProperty().addListener(((observableValue, s, t1) -> {
            System.out.println(t1);
        }));
    }

    @FXML
    void closeModal() {
        Window window = input_rfid.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    void checkRFID() {
        System.out.println(input_rfid.getText());
    }
}
