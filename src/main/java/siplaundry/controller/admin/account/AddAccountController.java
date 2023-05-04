package siplaundry.controller.admin.account;



import java.util.Set;

import jakarta.validation.ConstraintViolation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
// import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
// import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import siplaundry.data.AccountRole;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;
import siplaundry.util.ValidationUtil;
import siplaundry.view.AdminView;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class AddAccountController {

    private UsersRepo userRepo = new UsersRepo();

    @FXML
    private Button close_btn;

    @FXML
    private TextField TxtUserName;

    @FXML
    private TextField TxtFullName;

    @FXML
    private TextField TxtPhone;

    @FXML
    private TextField TxtPassword;

    @FXML
    private TextArea TxtAddress;

    private AccountRole Rbutton;

    @FXML
    private RadioButton RButtonCashier;

    @FXML
    private RadioButton RButtonAdmin;

    @FXML
    private HBox Btnsave;

    @FXML
    private ToggleGroup RoleUser;


    @FXML
    void closeModal() {
        Window window = close_btn.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
  
    @FXML
    public void initialize(){
        RoleUser.selectedToggleProperty().addListener((observable, oldValue, newValue) ->{
            if(newValue == RButtonAdmin){
                Rbutton = AccountRole.admin;
                System.out.println(Rbutton);
            }else if(newValue == RButtonCashier){
                Rbutton = AccountRole.cashier;
                System.out.println(Rbutton);
            }
        });
    }

     public void SaveAction(){
        UserEntity user = new UserEntity(
            TxtUserName.getText(),
            TxtFullName.getText(),
            TxtPhone.getText(),
            TxtPassword.getText(),
            TxtAddress.getText(),
            Rbutton
        );
  //      Set<ConstraintViolation<UserEntity>> vols = ValidationUtil.validate(user);

//        if(!vols.isEmpty()) {
//            System.out.println(ValidationUtil.getErrorsAsString(vols, "\n"));
//
//        }

         String title = "Tambah Akun";
         String message1 = "Akun berhasil ditmbahkan";

         TrayNotification tray = new TrayNotification();
         AnimationType type = AnimationType.POPUP;

        userRepo.add(user);
            tray.setAnimationType(type);
            tray.setTitle(title);
            tray.setMessage(message1);

            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.millis(500));

         Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2500), ae -> {
             try {
                 closeModal();
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }));
         timeline.play();
    }
    
}
