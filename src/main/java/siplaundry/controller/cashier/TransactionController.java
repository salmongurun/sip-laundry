package siplaundry.controller.cashier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kordamp.ikonli.javafx.FontIcon;

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
import siplaundry.data.SortingOrder;
import siplaundry.entity.TransactionEntity;
import siplaundry.repository.TransactionRepo;
import siplaundry.util.ViewUtil;
import siplaundry.view.admin.components.modal.ConfirmDialog;
import siplaundry.view.cashier.TransactionFormView;
import siplaundry.view.cashier.components.column.TransactionColumn;
import siplaundry.view.cashier.components.modal.BarcodeModal;
import siplaundry.view.util.EmptyData;
import toast.Toast;
import toast.ToastType;

public class TransactionController {
    private BorderPane parent_root, shadow_root;

    @FXML
    private VBox trans_table;

    @FXML
    private Text total_text;

    @FXML
    private TextField txt_keyword;

    @FXML
    private ComboBox<String> CB_column;

    @FXML
    private FontIcon sort_icon;

    private SortingOrder sortOrder = SortingOrder.DESC;
    private TransactionRepo transRepo = new TransactionRepo();
    private ArrayList<TransactionColumn> accColumns = new ArrayList<>();
    private String keyword;

    public TransactionController(BorderPane parent_root, BorderPane shadow_root) {
        this.parent_root = parent_root;
        this.shadow_root = shadow_root;
    }

    @FXML
    void initialize(){
        ObservableList<String> column = FXCollections.observableArrayList(
            "Nama Pelanggan",
            "Tanggal Transaksi",
            "Harga",
            "Status Cucian",
            "Status Pembayaran"
        );
        CB_column.setItems(column);

        List<TransactionEntity> trans = transRepo.get();
        showTable(trans);
    }

    @FXML
    void sortAction(){
        String column = "customers.name";
        this.sortOrder = ViewUtil.switchOrderIcon(this.sortOrder, this.sort_icon);
        
        if(CB_column.getValue() != null){
            if(CB_column.getValue().equals("Tanggal Transaksi")) column = "transactions.transaction_date";
            if(CB_column.getValue().equals("Status Cucian")) column = "transactions.status";
            if(CB_column.getValue().equals("Harga")) column = "transactions.amount";
            if(CB_column.getValue().equals("Status Pembayaran")) column = "transactions.payment_status";
        }

        List<TransactionEntity> trans = transRepo.sortTable(
            "customers.name", 
            " JOIN customers ON transactions.customer_id = customers.customer_id", 
            this.searchableValues(),
            column, 
            this.sortOrder
        );

        showTable(trans);
    }

    @FXML
    void searchAction(KeyEvent event){
        List<TransactionEntity> trans = transRepo.searchTable(
            "customers.name", 
            " JOIN customers ON transactions.customer_id = customers.customer_id", 
            this.searchableValues()
        );
        showTable(trans);
    }

    @FXML
    void showBarcodeModal() {
        new BarcodeModal(parent_root, shadow_root);
    }

    private HashMap<String, Object> searchableValues(){
        keyword = txt_keyword.getText();
        if(keyword.equals("lunas")){ keyword = "paid"; }
        if(keyword.equals("cicilan")){ keyword = "instalment"; }
        if(keyword.equals("belum lunas") || keyword.equals("belum")){ keyword = "unpaid"; }

        if(keyword.equals("proses")){ keyword = "process"; }
        if(keyword.equals("selesai")){ keyword = "finish"; }
        if(keyword.equals("diambil")){ keyword = "taken"; }

        return new HashMap<>(){{
            put("customers.name", keyword);
            put("transactions.payment_status", keyword);
            put("transactions.status", keyword);
        }};
    }

    public void showTable(List<TransactionEntity> trans){
        int i = 0;
        accColumns.clear();
        trans_table.getChildren().clear();

        if(trans == null) trans = transRepo.get();
            for(TransactionEntity transEn: trans){
                if(transEn.getUserID().getID().equals(SessionData.user.getID())){
                TransactionColumn column = new TransactionColumn(parent_root, shadow_root, this::showTable, transEn);

                if(i % 2 == 1) column.getStyleClass().add("stripped");

                trans_table.getChildren().add(column);
                accColumns.add(column);
                i++;
            }
        }
        
        int accTotal = trans_table.getChildren().size();

        if(accTotal < 1) 
            trans_table.getChildren().add(new EmptyData(this::showAddTransaction, txt_keyword.getText()));
        total_text.setText("Menampilkan total " + accTotal + " data akun");
    }

    @FXML
    void showAddTransaction() {
        this.parent_root.setCenter(new TransactionFormView(parent_root, shadow_root));
    }

}
