package siplaundry.view.admin.components.modal;

import jakarta.validation.ConstraintViolation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.*;
import siplaundry.data.AccountRole;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;
import siplaundry.util.ValidationUtil;
import toast.Toast;
import toast.ToastType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.kordamp.ikonli.javafx.FontIcon;

public class AccountModal {
    @FXML
    private Text modal_title, modal_subtitle;
    @FXML
    private Button close_btn;

    @FXML
    private TextField txt_fullname, txt_phone, txt_username;

    @FXML
    private HBox password_container;

    @FXML
    private TextArea txt_address;

    @FXML
    private RadioButton chs_admin, chs_cashier;

    @FXML
    private  ToggleGroup roleGroup;

    @FXML
    private FontIcon toggle_icon;

    private UsersRepo userRepo = new UsersRepo();
    private AccountRole accRole;
    private Node shadowRoot;
    private UserEntity account;
    private Consumer<List<UserEntity>> refreshTable;
    private Map<String, Node> fields;
    private String password;
    private boolean isPasswordShown = false;

    public AccountModal(Node shadowRoot, Consumer<List<UserEntity>> refreshTable, UserEntity account) {
        this.shadowRoot = shadowRoot;
        this.account = account;
        this.refreshTable = refreshTable;

        initModal();
    }

    @FXML
    public void initialize(){
        roleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if(newVal == chs_admin) accRole = AccountRole.admin;
            if(newVal == chs_cashier) accRole = AccountRole.cashier;
        });

        chs_admin.setSelected(true);
        if(this.account != null) {
            changeUpdate();
            password = this.account.getPassword();
        }

        this.fields = new HashMap<>() {{
            put("username", txt_username);
            put("fullname", txt_fullname);
            put("phone", txt_phone);
            put("password", password_container.getChildren().get(0));
            put("address", txt_address);
        }};
        addPasswordElement();
    }

    @FXML
    void closeModal() {
        Window window = close_btn.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    void saveAction() {
        if(account != null) {
            saveUpdate();
            return;
        }

        userRepo.add(validateAccount());

        (new Toast((AnchorPane) shadowRoot.getScene().getRoot()))
            .show(ToastType.SUCCESS, "Berhasil menambahkan akun", null);
        closeModal();
    }

    @FXML
    UserEntity validateAccount() {
        UserEntity user = new UserEntity(
            txt_username.getText(),
            txt_fullname.getText(),
            txt_phone.getText(),
            password,
            txt_address.getText(),
            accRole
        );

        Set<ConstraintViolation<UserEntity>> vols = ValidationUtil.validate(user);
        ValidationUtil.renderErrors(vols, this.fields);

        return user;
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
                refreshTable.accept(null);
            });

            shadowRoot.setVisible(true);
            modalStage.showAndWait();
        } catch(IOException e) { e.printStackTrace(); }
    }

    private void changeUpdate() {
        modal_title.setText("Edit Data Akun");
        modal_subtitle.setText("Perbarui Informasi");

        txt_username.setText(account.getUsername());
        txt_phone.setText(account.getPhone());
        txt_fullname.setText(account.getFullname());
        txt_address.setText(account.getAddress());

        if(account.getRole() == AccountRole.cashier) chs_cashier.setSelected(true);
    }

    private void saveUpdate() {
        account.setFullname(txt_fullname.getText());
        account.setPhone(txt_phone.getText());
        account.setUsername(txt_username.getText());
        account.setPassword(password);
        account.setAddress(txt_address.getText());
        account.setRole(accRole);

        validateAccount();
        userRepo.Update(account);

        new Toast((AnchorPane) shadowRoot.getScene().getRoot())
            .show(ToastType.SUCCESS, "Berhasil mengupdate akun", null);
        closeModal();
    }

    @FXML
    void togglePassword(){
        password_container.getChildren().remove(0);

        if(isPasswordShown){
            toggle_icon.setIconLiteral("bx-show");
            addPasswordElement();
        }else {
            toggle_icon.setIconLiteral("bx-hide");
            addvisiblePasswordElement();
        }

        isPasswordShown = !isPasswordShown;
    }

    private void addPasswordElement(){
        PasswordField field = new PasswordField();
        field.getStyleClass().add("input");
        field.setPrefSize(419, 45);
        field.setText(password);
        

        field.setOnKeyReleased(event -> {
            this.password = field.getText();
        });

        password_container.getChildren().add(0, field);
    }

    private void addvisiblePasswordElement(){
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
