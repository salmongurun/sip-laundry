package siplaundry.controller.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javafx.stage.WindowEvent;

import jakarta.validation.ConstraintViolation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import siplaundry.entity.PasswordEntity;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;
import siplaundry.util.ValidationUtil;
import toast.Toast;
import toast.ToastType;

public class ChangePassController {
    @FXML
    private PasswordField txt_pass, txt_confirmPass;

    @FXML
    private Button btn_reset, close_btn;

    private Node shadowRoot;
    private Map<String, Node> fields;
    private UserEntity user;
    private UsersRepo userRepo = new UsersRepo();

    public ChangePassController(Node shadowRoot, UserEntity user){
        this.shadowRoot = shadowRoot;
        this.user = user;

        initModal();
    }

    @FXML
    void initialize(){
        this.fields = new HashMap<>(){{
            put("password", txt_pass);
            put("confirm", txt_confirmPass);
        }};
    }

    private void initModal(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/auth/editProfModal.fxml"));
        loader.setController(this);

        try {
            Parent root = loader.load();
            Stage modalStage = new Stage();
            Scene modalScene = new Scene(root);

            modalScene.setFill(Color.TRANSPARENT);
            modalStage.setScene(modalScene);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initStyle(StageStyle.TRANSPARENT);
            modalStage.setResizable(false);

            modalStage.setOnCloseRequest(evt -> {
                shadowRoot.setVisible(false);
            });

            shadowRoot.setVisible(true);
            modalStage.showAndWait();
        } catch(IOException e) { e.printStackTrace(); }
    }

    @FXML
    void changePassword(){
        PasswordEntity password = new PasswordEntity(
            txt_pass.getText(),
            txt_confirmPass.getText()
        );

        Set<ConstraintViolation<PasswordEntity>> vols = ValidationUtil.validate(password);
        ValidationUtil.renderErrors(vols, this.fields);

        EditProfileController.password = password.getPassword();
        // user.setPassword(password.getPassword());
        // userRepo.Update(user);

        (new Toast((AnchorPane) shadowRoot.getScene().getRoot()))
            .show(ToastType.SUCCESS, "Berhasil mengganti Password", null);
        closeModal();
    }

    @FXML
    void closeModal(){
        Window window = close_btn.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

}
