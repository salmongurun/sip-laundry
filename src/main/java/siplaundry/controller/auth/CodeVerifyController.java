package siplaundry.controller.auth;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import siplaundry.entity.UserEntity;
import siplaundry.entity.VerificationEntity;
import siplaundry.repository.VerificationRepo;
import siplaundry.util.NumberUtil;
import toast.Toast;
import toast.ToastType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CodeVerifyController {
    @FXML
    private HBox close_btn;

    @FXML
    private Text phone_number;

    @FXML
    private TextField code_input;
    @FXML
    private AnchorPane rootAnchor;

    private UserEntity user;
    private VerificationRepo verifyRepo = new VerificationRepo();

    public CodeVerifyController(UserEntity user) {
        this.user = user;
    }

    @FXML
    void initialize() {
        phone_number.setText("+" + NumberUtil.maskPhoneNumber(user.getPhone()));
    }

    @FXML
    void checkVerificationCode() {
        Stage stage = (Stage) close_btn.getScene().getWindow();
        Stage parentStage = (Stage) stage.getOwner();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/auth/reset-password.fxml"));

        loader.setController(new ResetPasswordController(user));

        List<VerificationEntity> verifyData = verifyRepo.get(new HashMap<>() {{
            put("user_id", user.getID());
            put("code", code_input.getText());
        }});

        if(verifyData.size() < 1) {
            Toast toast = new Toast(rootAnchor);
            String message = (code_input.getText().isBlank()) ? "Kode verifikasi harus diisi" : "Kode verifikasi tidak sesuai";

            toast.show(ToastType.FAILED, message, null);
            return;
        }

        verifyRepo.delete(verifyData.get(0));

        try {
            Parent root = loader.load();
            parentStage.setScene(new Scene(root));
            stage.close();
        } catch(IOException e) { e.printStackTrace(); }
    }

    @FXML
    void closeModal() {
        Window window = close_btn.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
