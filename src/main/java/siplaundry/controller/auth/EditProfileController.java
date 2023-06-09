package siplaundry.controller.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kordamp.ikonli.javafx.FontIcon;

import jakarta.validation.ConstraintViolation;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import siplaundry.data.SessionData;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;
import siplaundry.util.ValidationUtil;
import toast.Toast;
import toast.ToastType;

public class EditProfileController {
    @FXML
    private HBox btn_delete, btn_save, password_container;

    @FXML
    private TextField txt_fullname, txt_username, txt_phone;

    @FXML
    private TextArea txt_address;

    @FXML
    private FontIcon toggle_icon;

    @FXML
    private PasswordField txt_password;

    private UsersRepo userRepo = new UsersRepo();
    private UserEntity user = SessionData.user;
    private Map<String, Node> fields;
    private BorderPane shadowRoot;
    public static String password;

    public EditProfileController(BorderPane shadow){ this.shadowRoot = shadow; }

    @FXML
    public void initialize(){
        password = user.getPassword();

        txt_fullname.setText(user.getFullname());
        txt_username.setText(user.getUsername());
        txt_phone.setText(user.getPhone());
        txt_password.setText(password);
        txt_address.setText(user.getAddress());

        this.fields = new HashMap<>(){{
            put("userName", txt_username);
            put("fullname", txt_fullname);
            put("phone", txt_phone);
            put("password", txt_password);
            put("address", txt_address);
        }};

    }


    @FXML
    private void updateAction(){
        user.setFullname(txt_fullname.getText());
        user.setUsername(txt_username.getText());
        user.setAddress(txt_address.getText());
        user.setPhone(txt_phone.getText());
        user.setPassword(password);

        validateAccount();
        userRepo.Update(user);
        
        new Toast((AnchorPane) shadowRoot.getScene().getRoot())
            .show(ToastType.SUCCESS, "Berhasil mengupdate akun", null);
        
    }

    @FXML
    UserEntity validateAccount(){
        UserEntity userNew = new UserEntity(
            txt_username.getText(),
            txt_fullname.getText(),
            txt_phone.getText(),
            txt_password.getText(),
            txt_address.getText(),
            user.getRole()
        );

        Set<ConstraintViolation<UserEntity>> vols = ValidationUtil.validate(userNew);
        ValidationUtil.renderErrors(vols, this.fields);

        return userNew;
    }

    @FXML
    private void changePassword(){
        new ChangePassController(shadowRoot, null);
    }

    @FXML
    private void clearAction(){
        txt_fullname.setText("");
        txt_username.setText("");
        txt_password.setText("");
        txt_phone.setText("");
        txt_address.setText("");
    }

}
