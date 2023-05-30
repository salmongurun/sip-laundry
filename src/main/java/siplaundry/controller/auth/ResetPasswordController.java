package siplaundry.controller.auth;

import jakarta.validation.ConstraintViolation;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import siplaundry.data.SessionData;
import siplaundry.entity.PasswordEntity;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;
import siplaundry.util.ValidationUtil;
import siplaundry.util.ViewUtil;
import toast.Toast;
import toast.ToastType;

import java.util.HashMap;
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
        PasswordEntity password = new PasswordEntity(
            txt_password.getText(),
            txt_confirm_password.getText()
        );

        Set<ConstraintViolation<PasswordEntity>> vols = ValidationUtil.validate(password);
        ValidationUtil.renderErrors(vols, this.fields);

        user.setPassword(password.getPassword());
        userRepo.Update(user);

        new Toast((AnchorPane) txt_password.getScene().getRoot())
            .show(ToastType.SUCCESS, "Berhasil mereset password", () -> {
                Stage stage = (Stage) txt_password.getScene().getWindow();
                SessionData.user = user;
                ViewUtil.authRedirector(stage);
            });
    }
}