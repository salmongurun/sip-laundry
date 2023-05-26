package siplaundry.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import siplaundry.data.SessionData;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;
import siplaundry.view.admin.components.column.AccountColumn;
import siplaundry.view.admin.components.modal.AccountModal;

import java.util.HashMap;
import java.util.List;

public class AccountController {
    private BorderPane shadowRoot;

    @FXML
    private HBox btn_add_account;

    @FXML
    private VBox account_table;

    @FXML
    private Text total_text;
    @FXML
    private TextField txt_keyword;

    private UsersRepo userRepo = new UsersRepo();

    public AccountController(BorderPane shadow) {
        this.shadowRoot = shadow;
    }

    @FXML
    void initialize() {
        System.out.println("Sip");
        List<UserEntity> users = userRepo.get();
        showTable(users);
    }

    @FXML
    void showAddAccount(MouseEvent event) {
        new AccountModal(shadowRoot, this::showTable, null);
    }

    @FXML
    void searchAction(KeyEvent event) {
        String keyword = txt_keyword.getText();
        List<UserEntity> users = userRepo.search(new HashMap<>() {{
            put("fullname", keyword);
            put("phone", keyword);
            put("username", keyword);
        }});

        showTable(users);
    }

    public void showTable(List<UserEntity> users) {
        account_table.getChildren().clear();

        if(users == null) users = userRepo.get();
        for(UserEntity user: users) {
            if(!user.getFullname().equals(SessionData.user.getFullname())) {
                account_table.getChildren().add(new AccountColumn(shadowRoot, this::showTable, user));
            }
         }

        total_text.setText("Menampilkan total "+ users.size() +" data akun");
    }
}
