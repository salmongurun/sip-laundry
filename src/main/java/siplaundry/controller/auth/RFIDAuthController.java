package siplaundry.controller.auth;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
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
    private boolean isScanMode = true;

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
    void checkRFID() {
        if(!isScanMode) {
            return;
        }

        rfid_scan_text.setText("Memindai...");
        String rfid = input_rfid.getText().replaceAll("\\s+", " ");

        rfid_container.getStyleClass().remove("success");
        rfid_container.getStyleClass().remove("failed");

        if(rfid.length() < 10) {
            return;
        }

        isScanMode = false;

        List<UserEntity> users = userRepo.get(new HashMap<>() {{
            put("rfid", rfid);
        }});

        if(users.size() > 0) {
            rfid_container.getStyleClass().add("success");
            rfid_scan_text.setText("Berhasil mengautentikasi");

            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> {
                closeModal();
                SessionData.user = users.get(0);
                ViewUtil.authRedirector(primaryStage);
            }));

            timeline.play();

            return;
        }

        rfid_container.getStyleClass().add("failed");
        rfid_scan_text.setText("Gagal mengautentikasi");

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> {
            resetScan();
        }));

        timeline.play();
    }

    private void resetScan() {
        input_rfid.setText("");
        rfid_scan_text.setText("Menunggu kartu...");
        rfid_container.getStyleClass().remove("success");
        rfid_container.getStyleClass().remove("failed");

        isScanMode = true;
    }
}
