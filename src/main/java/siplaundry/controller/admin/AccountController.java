package siplaundry.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;
import siplaundry.view.admin.components.column.AccountColumn;
import siplaundry.view.admin.components.modal.AccountModal;

import java.util.List;

public class AccountController {
    private BorderPane shadowRoot;

    @FXML
    private HBox btn_add_account;

    @FXML
    private VBox account_table;

    @FXML
    private Text total_text;

    private UsersRepo userRepo = new UsersRepo();

    public AccountController(BorderPane shadow) {
        this.shadowRoot = shadow;
    }

    @FXML
    void initialize() {
        List<UserEntity> users = userRepo.get();

        for(UserEntity user: users) {
            this.account_table.getChildren().add(new AccountColumn(shadowRoot, user));
        }

        total_text.setText("Menampilkan total "+ users.size() +" data akun");
    }

    @FXML
    void showAddAccount(MouseEvent event) {
        new AccountModal(shadowRoot, null);
    }
}
