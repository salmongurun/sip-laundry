package siplaundry.controller.cashier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import siplaundry.data.SessionData;
import siplaundry.entity.TransactionEntity;
import siplaundry.repository.TransactionRepo;
import siplaundry.view.admin.components.modal.ConfirmDialog;
import siplaundry.view.cashier.TransactionFormView;
import siplaundry.view.cashier.components.column.TransactionColumn;
import toast.Toast;
import toast.ToastType;

public class TransactionController {
    private BorderPane parent_root, shadow_root;

    @FXML
    private HBox btn_bulk_delete;

    @FXML
    private VBox trans_table;

    @FXML
    private Text total_text;

    @FXML
    private TextField txt_keyword;

    @FXML
    private ComboBox<String> CB_sortBy, CB_column;

    private TransactionRepo transRepo = new TransactionRepo();
    private Set<TransactionEntity> bulkItems = new HashSet<>();
    private ArrayList<TransactionColumn> accColumns = new ArrayList<>();

    public TransactionController(BorderPane parent_root, BorderPane shadow_root) {
        this.parent_root = parent_root;
        this.shadow_root = shadow_root;
    }

    @FXML
    void initialize(){
        ObservableList<String> items = FXCollections.observableArrayList(
            "A-Z",
            "Z-A"
        );
        CB_sortBy.setItems(items);

        ObservableList<String> column = FXCollections.observableArrayList(
            "Nama Pelanggan",
            "Tanggal Transaksi"
        );
        CB_column.setItems(column);

        List<TransactionEntity> trans = transRepo.get();
        showTable(trans);
    }

    @FXML
    void SortAction(){
        String SortBy = " DESC";
        String column = " pickup_date";

        if(CB_sortBy.getValue().equals("A-Z")) SortBy = " ASC";
        if(CB_column.getValue().equals("Nama Pelanggan")) column = " customers.name";

        List<TransactionEntity> trans = transRepo.sortTable(
            "customers.name", 
            " JOIN customers ON transactions.customer_id = customers.customer_id", 
            column, 
            SortBy
        );

        showTable(trans);
    }

    @FXML
    void searchAction(KeyEvent event){
        String keyword = txt_keyword.getText();

        List<TransactionEntity> trans = transRepo.searchTable(
            "customers.name", 
            " JOIN customers ON transactions.customer_id = customers.customer_id", 
            new HashMap<>(){{
                put("cuatomers.name", keyword);
                put("transacctions.payment_status", keyword);
            }}
        );
        showTable(trans);
    }

    @FXML
    void selectBulkAll(){
        for(TransactionColumn column: accColumns){
            column.toggleBulk();
        }
    }

    @FXML
    void bulkDelete(){
        new ConfirmDialog(shadow_root, () -> {
            for(TransactionEntity trans: this.bulkItems){
                transRepo.delete(trans.getid());
            }

            new Toast((AnchorPane) btn_bulk_delete.getScene().getRoot())
                    .show(ToastType.SUCCESS, "Berhasil melakukan hapus semua", null);
            showTable(transRepo.get());
        });
    }

    public void showTable(List<TransactionEntity> trans){
        accColumns.clear();
        trans_table.getChildren().clear();

        if(trans == null) trans = transRepo.get();
        for(TransactionEntity transEn: trans){
                if(!(transEn.getUserID().getID().equals(SessionData.user.getID()))) continue;

                TransactionColumn column = new TransactionColumn(shadow_root, this::showTable, transEn);
                column.setBulkAction(this::toggleBulkItem);

                trans_table.getChildren().add(column);
                accColumns.add(column);
            
        }
        total_text.setText("Menampilkan total " + trans.size() + " data akun");
    }

    protected void toggleBulkItem(TransactionEntity trans){
        if(this.bulkItems.contains(trans)){
            this.bulkItems.remove(trans);
        } else {
            this.bulkItems.add(trans);
        }

        btn_bulk_delete.setDisable(this.bulkItems.size() < 1);
    }

    @FXML
    void showAddTransaction() {
        this.parent_root.setCenter(new TransactionFormView(parent_root, shadow_root));
    }

}
