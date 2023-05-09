package siplaundry.controller.admin.account;

import jakarta.validation.ConstraintViolation;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import siplaundry.data.AccountRole;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;

import siplaundry.util.ValidationUtil;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.util.Set;

public class AccountModalController {
    @FXML
    private Button close_btn;

    @FXML
    private TextField txt_fullname, txt_phone, txt_username;

    @FXML
    private PasswordField txt_password;

    @FXML
    private TextArea txt_address;

    @FXML
    private RadioButton chs_admin, chs_cashier;

    @FXML
    private  ToggleGroup roleGroup;

    private UsersRepo userRepo = new UsersRepo();
    private TrayNotification trayNotif = new TrayNotification();
    private AccountRole accRole;

    @FXML
    void closeModal() {
        Window window = close_btn.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    public void initialize(){
        chs_admin.setSelected(true);

        roleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if(newVal == chs_admin) accRole = AccountRole.admin;
            if(newVal == chs_cashier) accRole = AccountRole.cashier;
        });
    }
}
