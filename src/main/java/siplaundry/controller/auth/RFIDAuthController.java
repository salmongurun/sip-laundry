package siplaundry.controller.auth;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import siplaundry.data.SessionData;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;
import siplaundry.util.ViewUtil;

import java.util.HashMap;
import java.util.List;

public class RFIDAuthController {
    @FXML
    private TextField input_rfid;
    @FXML
    private HBox rfid_container;
    @FXML
    private Text rfid_scan_text;

    private UsersRepo userRepo = new UsersRepo();
    private Stage primaryStage;

    public RFIDAuthController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    void initialize() throws InterruptedException {
        input_rfid.requestFocus();
    }

    @FXML
    void closeModal() {
        Window window = input_rfid.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    void checkRFID() throws InterruptedException {
        rfid_scan_text.setText("Memindai...");
        String rfid = input_rfid.getText();

        rfid_container.getStyleClass().remove("success");
        rfid_container.getStyleClass().remove("failed");

        if(rfid.length() < 10) {
            return;
        }

        List<UserEntity> users = userRepo.get(new HashMap<>() {{
            put("rfid", rfid.replaceAll("\\s+", " "));
        }});

        System.out.println(rfid.replaceAll("\\s+", " "));

        if(users.size() > 0) {
            rfid_container.getStyleClass().add("success");
            rfid_scan_text.setText("Berhasil mengautentikasi");

            closeModal();
            SessionData.user = users.get(0);
            ViewUtil.authRedirector(primaryStage);
            return;
        }

        rfid_container.getStyleClass().add("failed");
        rfid_scan_text.setText("Gagal mengautentikasi");
    }
}
