package siplaundry.controller.admin.account;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import siplaundry.data.AccountRole;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;

import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class AddAccountController {
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
        roleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if(newVal == chs_admin) accRole = AccountRole.admin;
            if(newVal == chs_cashier) accRole = AccountRole.cashier;
        });
    }

    @FXML
    void saveAction() {
        UserEntity user = new UserEntity(
            txt_username.getText(),
            txt_fullname.getText(),
            txt_phone.getText(),
            txt_password.getText(),
            txt_address.getText(),
            accRole
        );

        userRepo.add(user);
        showToast("Sukses", "Berhasil menambahkan akun", NotificationType.SUCCESS);
        closeModal();
    }

    void showToast(String title, String message, NotificationType type) {
        trayNotif.setAnimationType(AnimationType.POPUP);
        trayNotif.setTitle(title);
        trayNotif.setMessage(message);

        trayNotif.setNotificationType(type);
        trayNotif.showAndDismiss(Duration.millis(1000));
    }
}
