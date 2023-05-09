package siplaundry.view.admin.components.modal;

import jakarta.validation.ConstraintViolation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import siplaundry.data.AccountRole;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;
import siplaundry.util.ValidationUtil;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.util.Set;

public class AccountModal {
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
    private Node shadowRoot;
    private UserEntity account;

    public AccountModal(Node shadowRoot, UserEntity account) {
        this.shadowRoot = shadowRoot;
        this.account = account;
        initModal();
    }

    @FXML
    public void initialize(){
        roleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if(newVal == chs_admin) accRole = AccountRole.admin;
            if(newVal == chs_cashier) accRole = AccountRole.cashier;
        });

        chs_admin.setSelected(true);
    }

    @FXML
    void closeModal() {
        Window window = close_btn.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
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

        Set<ConstraintViolation<UserEntity>> vols = ValidationUtil.validate(user);

        for(ConstraintViolation<UserEntity> vol: vols) {
            if(vol.getPropertyPath().toString().equals("username")) txt_username.getStyleClass().add("error");
            if(vol.getPropertyPath().toString().equals("fullname")) txt_fullname.getStyleClass().add("error");
        }

//        if(vols.size() > 0) return;

        userRepo.add(user);
        trayNotif.setTray("Sukses", "Berhasil menambahkan akun", NotificationType.SUCCESS, AnimationType.POPUP);
        closeModal();
    }

    private void initModal() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/account/add.fxml"));
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
}
