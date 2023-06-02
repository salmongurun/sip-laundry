package siplaundry.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import siplaundry.data.SessionData;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;
import siplaundry.view.admin.components.column.AccountColumn;
import siplaundry.view.admin.components.modal.AccountModal;
import siplaundry.view.admin.components.modal.ConfirmDialog;
import siplaundry.view.util.EmptyData;
import toast.Toast;
import toast.ToastType;

import java.util.*;

public class AccountController {
    @FXML
    private HBox btn_add_account, btn_bulk_delete;
    @FXML
    private VBox account_table;
    @FXML
    private Text total_text;
    @FXML
    private TextField txt_keyword;
    @FXML
    private ComboBox<String> CB_sortBy;
    @FXML
    private ComboBox<String> CB_column;

    private BorderPane shadowRoot;
    private UsersRepo userRepo = new UsersRepo();
    private Set<UserEntity> bulkItems = new HashSet<>();
    private ArrayList<AccountColumn> accColumns = new ArrayList<>();
    
    public AccountController(BorderPane shadow) {
        this.shadowRoot = shadow;
    }

    @FXML
    void initialize() {
        ObservableList<String> items = FXCollections.observableArrayList(
            "A-Z",
            "Z-A"
        );
        CB_sortBy.setItems(items);

        ObservableList<String> column = FXCollections.observableArrayList(
            "Role",
            "Username",
            "Nama lengkap"
        );
        CB_column.setItems(column);

        List<UserEntity> users = userRepo.get();
        showTable(users);
    }

    @FXML
    void showAddAccount() {
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

    @FXML
    void SortAction(){
        String SortBy = " DESC";
        String column = " fullname";
        
        if(CB_sortBy.getValue().equals("A-Z")) SortBy = " ASC";
        if(CB_column.getValue().equals("Username")) column = " username";
        if(CB_column.getValue().equals(" Role")) column = " role";

        List<UserEntity> users = userRepo.sortBy(column, SortBy);
        showTable(users);
     }

     @FXML
     void selectBulkAll() {
        for(AccountColumn column: accColumns) {
            column.toggleBulk();
        }
     }

     @FXML
     void bulkDelete() {
        new ConfirmDialog(shadowRoot, () -> {
            for(UserEntity user: this.bulkItems) {
                userRepo.delete(user.getID());
            }

            new Toast((AnchorPane) btn_bulk_delete.getScene().getRoot())
                    .show(ToastType.SUCCESS, "Berhasil melakukan hapus semua", null);
            showTable(userRepo.get());
        });
     }

    public void showTable(List<UserEntity> users) {
        accColumns.clear();
        account_table.getChildren().clear();

        if(users == null) users = userRepo.get();

        if(users.size() < 2){
            account_table.getChildren().add(new EmptyData(this::showAddAccount, txt_keyword.getText()));
        }

        for(UserEntity user: users) {
            if(user.getID().equals(SessionData.user.getID())) continue;

            AccountColumn column = new AccountColumn(shadowRoot, this::showTable, user);
            column.setBulkAction(this::toggleBulkItem);

            account_table.getChildren().add(column);
            accColumns.add(column);
         }

        total_text.setText("Menampilkan total "+ users.size() +" data akun");
    }

    protected void toggleBulkItem(UserEntity user) {
        if (this.bulkItems.contains(user)) {
            this.bulkItems.remove(user);
        } else {
            this.bulkItems.add(user);
        }

        btn_bulk_delete.setDisable(this.bulkItems.size() < 1);
    }
}
