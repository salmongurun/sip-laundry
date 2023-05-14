package siplaundry.view.admin.components.column;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import siplaundry.data.AccountRole;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;
import siplaundry.view.admin.components.modal.AccountModal;
import siplaundry.view.admin.components.modal.ConfirmDialog;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class AccountColumn extends HBox {
    @FXML
    private Text txt_username, txt_phone, txt_fullname, txt_role;

    @FXML
    private HBox role_background, edit_btn, delete_btn;

    private UserEntity user;
    private UsersRepo userRepo = new UsersRepo();
    private BorderPane shadowRoot;

    private Consumer<List<UserEntity>> refreshTable;

    public AccountColumn(BorderPane shadowRoot, Consumer<List<UserEntity>> refreshTable, UserEntity user) {
        this.user = user;
        this.shadowRoot = shadowRoot;
        this.refreshTable = refreshTable;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/account/column.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch(IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {
        setRoleColor();

        txt_fullname.setText(user.getFullname());
        txt_username.setText(user.getUsername());
        txt_phone.setText(user.getPhone());
        txt_role.setText(user.getRole().toString());

        edit_btn.setOnMouseClicked(event -> {
            new AccountModal(shadowRoot, refreshTable, user);
        });

        delete_btn.setOnMouseClicked(event -> {
            new ConfirmDialog(shadowRoot, () -> {
                userRepo.delete(user.getID());
                refreshTable.accept(null);
            });
        });
    }

    void setRoleColor() {
        if(user.getRole() == AccountRole.cashier) {
            txt_role.setStyle("-fx-fill: #6A9A98;");
            role_background.setStyle(role_background.getStyle() + "-fx-background-color: #f0f4f4;");
        }
    }
}
