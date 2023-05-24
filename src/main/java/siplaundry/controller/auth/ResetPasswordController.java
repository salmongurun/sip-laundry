package siplaundry.controller.auth;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.metadata.ConstraintDescriptor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import siplaundry.entity.UserEntity;
import siplaundry.util.ValidationUtil;
import siplaundry.util.violations.MatchViolation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ResetPasswordController {
    @FXML
    private PasswordField txt_password, txt_confirm_password;
    private UserEntity user;
    private Map<String, Node> fields;

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

        if(!txt_confirm_password.getText().equals(txt_password.getText())) {
            vols.add(new MatchViolation<>("confirm", "Konfirmasi Password"));
        }

        ValidationUtil.renderErrors(vols, this.fields);
    }
}

class PasswordEntity {
    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 8, message = "Password minimal 8 karakter")
    private String password;

    @NotBlank(message = "Konfirmasi password tidak boleh kosong")
    private String confirm;
    public PasswordEntity(String password, String confirm) {
        this.password = password;
        this.confirm = confirm;
    }
}