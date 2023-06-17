package siplaundry.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;
import siplaundry.data.SessionData;
import siplaundry.data.SortingOrder;
import siplaundry.entity.UserEntity;
import siplaundry.repository.UsersRepo;
import siplaundry.util.ViewUtil;
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
    private ComboBox<String> CB_column;
    @FXML
    private FontIcon sort_icon;

    private BorderPane shadowRoot;
    private UsersRepo userRepo = new UsersRepo();
    private Set<UserEntity> bulkItems = new HashSet<>();
    private ArrayList<AccountColumn> accColumns = new ArrayList<>();
    private SortingOrder sortOrder = SortingOrder.DESC;

    private String keyword;
    
    public AccountController(BorderPane shadow) {
        this.shadowRoot = shadow;
    }

    @FXML
    void initialize() {
        List<UserEntity> users = userRepo.get();
        ObservableList<String> column = FXCollections.observableArrayList(
            "Role",
            "Username",
            "Nama lengkap"
        );

        CB_column.setItems(column);
        showTable(users);
    }

    @FXML
    void showAddAccount() {
        new AccountModal(shadowRoot, this::showTable, null);
    }

    @FXML
    void searchAction(KeyEvent event) {
        List<UserEntity> users = userRepo.search(this.searchableValues());
        showTable(users);
    }

    private HashMap<String, Object> searchableValues(){
        keyword = txt_keyword.getText();
        if(keyword.equals("kasir")){ keyword = "cashier";}

        return new HashMap<>(){{
            put("fullname", keyword);
            put("phone", keyword);
            put("username", keyword); 
            put("role", keyword); 
        }};
    }

    @FXML
    void sortAction(){
        String column = "role";
        this.sortOrder = ViewUtil.switchOrderIcon(this.sortOrder, this.sort_icon);

        if(CB_column.getValue() != null) {
            if(CB_column.getValue().equals("Username")) column = "username";
            if(CB_column.getValue().equals("Nama Lengkap")) column = "fullname";
        }

        List<UserEntity> users = userRepo.search(this.searchableValues(),column,this.sortOrder);
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
        int i = 0;

        accColumns.clear();
        account_table.getChildren().clear();

        if(users == null) users = userRepo.get();

        for(UserEntity user: users) {
            if(user.getID().equals(SessionData.user.getID())) continue;

            AccountColumn column = new AccountColumn(shadowRoot, this::showTable, user);
            column.setBulkAction(this::toggleBulkItem);

            if(i % 2 == 1) column.getStyleClass().add("stripped");

            account_table.getChildren().add(column);
            accColumns.add(column);

            i++;
        }

        int accTotal = account_table.getChildren().size();

        if(accTotal < 1)
            account_table.getChildren().add(new EmptyData(this::showAddAccount, txt_keyword.getText()));

        total_text.setText("Menampilkan total "+ accTotal +" data akun");
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
