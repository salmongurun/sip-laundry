package siplaundry.controller.admin.account;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;
import siplaundry.view.util.WarningModal;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class AddRfidController {
    @FXML
    private TextField input_rfid;
    @FXML
    private HBox rfid_container;
    @FXML
    private Text rfid_scan_text;

    private Stage primaryStage;
    private Node shadowRoot;
    private Consumer<String> addAction;
    private UsersRepo userRepo = new UsersRepo();

    public AddRfidController(Node shadowRoot, Stage primaryStage, Consumer<String> addAction){
        this.primaryStage = primaryStage;
        this.shadowRoot = shadowRoot;
        this.addAction = addAction;
    }

    @FXML
    void initialize() throws InterruptedException{

    }

    @FXML
    void checkRFID(){
        rfid_scan_text.setText("Memindai...");
        String rfid = input_rfid.getText().replaceAll("\\s+", " ");

        rfid_container.getStyleClass().remove("success");
        rfid_container.getStyleClass().remove("failed");

        if(rfid.length() < 10) {
            return;
        }
        List<UserEntity> users = userRepo.get(new HashMap<>() {{
            put("rfid", rfid);
        }});

        if(users.size() > 0) {
            new WarningModal((BorderPane) shadowRoot, "RFID telah terdaftar");
            input_rfid.clear();
            closeModal();
            return;
        }

        addAction.accept(rfid);
        closeModal();
    }

    @FXML
    void closeModal(){
        Window window = input_rfid.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
