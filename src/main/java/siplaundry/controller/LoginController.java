package siplaundry.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import siplaundry.entity.TransactionEntity;
import siplaundry.repository.TransactionDetailRepo;
import siplaundry.repository.TransactionRepo;
import siplaundry.service.AuthService;
import siplaundry.util.ViewUtil;
import siplaundry.view.auth.RFIDAuthView;
import siplaundry.view.auth.VerificationView;
import siplaundry.view.print.ReceiptPrint;
import toast.Toast;
import toast.ToastType;

import java.io.IOException;
import java.util.HashMap;

import org.kordamp.ikonli.javafx.FontIcon;

public class LoginController {

    @FXML
    private Button BtnLogin;
    @FXML
    private TextField TxtUserName;
    @FXML
    private BorderPane shadowRoot;
    @FXML
    private HBox rfid_btn;
    @FXML
    private HBox password_container;
    @FXML
    private FontIcon toggle_icon;
    @FXML
    private Text welcome_text;

    private String password;
    private boolean isPasswordShown = false;

    public void initialize() {
        shadowRoot.setVisible(false);
        addPasswordElement();

        TransactionEntity transaction = new TransactionRepo().get(195);

        welcome_text.setOnMouseClicked(event -> {
            new ReceiptPrint(transaction, new TransactionDetailRepo().get(new HashMap<>() {{
                put("transaction_id", transaction.getid());
            }}));
        });
    }

    public void ButtonLoginAction() {
        AnchorPane rootNode = (AnchorPane) shadowRoot.getScene().getRoot();
        Stage stage = (Stage) TxtUserName.getScene().getWindow();
        Toast toast = new Toast(rootNode);

         if(!new AuthService().login(TxtUserName.getText(), password)){
            toast.show(ToastType.FAILED, "Username atau password salah", null);
            return;
         }

         toast.setDuration(1)
            .show(ToastType.SUCCESS, "Berhasil login", () -> {
                 ViewUtil.authRedirector(stage);
            });
    }

    @FXML
    void showVerification(MouseEvent event) {
        new VerificationView(shadowRoot, (Stage) shadowRoot.getScene().getWindow());
    }

    @FXML
    void showRFIDAuth() {
        new RFIDAuthView(shadowRoot, (Stage) shadowRoot.getScene().getWindow());
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

    @FXML
    void togglePassword() {
        password_container.getChildren().remove(0);

        if(isPasswordShown) {
            toggle_icon.setIconLiteral("bx-show");
            addPasswordElement();
        } else {
            toggle_icon.setIconLiteral("bx-hide");
            addVisiblePasswordElement();
        }

        isPasswordShown = !isPasswordShown;

    }

    private void addPasswordElement() {
        PasswordField field = new PasswordField();
        field.getStyleClass().add("input");
        field.setPrefSize(419, 45);
        field.setText(password);

        field.setOnKeyReleased(event -> {
            this.password = field.getText();
        });

        password_container.getChildren().add(0, field);
    }

    private void addVisiblePasswordElement() {
        TextField field = new TextField();
        field.getStyleClass().add("input");
        field.setPrefSize(419, 45);
        field.setText(password);

        field.setOnKeyReleased(event -> {
            this.password = field.getText();
        });

        password_container.getChildren().add(0, field);
    }
}
