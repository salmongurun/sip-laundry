package siplaundry.controller.admin;

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
import siplaundry.data.SortingOrder;
import siplaundry.entity.TransactionEntity;
import siplaundry.repository.TransactionRepo;
import siplaundry.util.ViewUtil;
import siplaundry.view.admin.components.column.TransactionColumn;
import siplaundry.view.admin.components.modal.ConfirmDialog;
import siplaundry.view.util.EmptyData;
import toast.Toast;
import toast.ToastType;

public class TransactionController {

    @FXML
    private HBox btn_bulk_delete;

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

    private BorderPane shadowRoot;
    private TransactionRepo transRepo = new TransactionRepo();
    private Set<TransactionEntity> bulkItems = new HashSet<>();
    private ArrayList<TransactionColumn> accColumns = new ArrayList<>();

    private String keyword;
    public TransactionController(BorderPane shadow){
        this.shadowRoot = shadow;
    }

    @FXML
    void initialize(){
        ObservableList<String> column = FXCollections.observableArrayList(
            "Nama Kasir",
            "Nama Pelanggan",
            "Tanggal Transaksi",
            "Status Cucian",
            "Status Pembayaran",
            "Harga"
        );
        CB_column.setItems(column);

        List<TransactionEntity> trans = transRepo.get();
        showTable(trans);
    }

    @FXML
    void sortAction(){
        String column = "users.fullname";

        this.sortOrder = ViewUtil.switchOrderIcon(this.sortOrder, this.sort_icon);

        if(CB_column.getValue() != null){
            if(CB_column.getValue().equals("Nama Pelanggan")) column = " customers.name";
            if(CB_column.getValue().equals("Tanggal Transaksi")) column = "transaction_date";
            if(CB_column.getValue().equals("Status Cucian")) column = "transactions.status";
            if(CB_column.getValue().equals("Status Pembayaran")) column = "transactions.payment_status";
            if(CB_column.getValue().equals("Harga")) column = "transactions.amount";
        }


        List<TransactionEntity> trans = transRepo.sortTable(
            "transactions.status, transactions.payment_status, users.fullname, customers.name", 
            " JOIN users ON transactions.user_id = users.user_id JOIN customers ON transactions.customer_id = customers.customer_id", 
            this.searchableValues(),
            column, 
            this.sortOrder
        );

        showTable(trans);
    }

    @FXML
    void searchAction(KeyEvent event){
        List<TransactionEntity> trans = transRepo.searchTable(
            "users.fullname, customers.name", 
            " JOIN users ON transactions.user_id = users.user_id JOIN customers ON transactions.customer_id = customers.customer_id ", 
            this.searchableValues()
        );
        showTable(trans);
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
            put("users.fullname", keyword);
            put("customers.name", keyword);
            put("transactions.payment_status", keyword);
            put("transactions.status", keyword);
        }};
    }

    @FXML
    void selectBulkAll(){
        for(TransactionColumn column: accColumns){
            column.toggleBulk();
        }
    }

    @FXML
    void bulkDelete(){
        new ConfirmDialog(shadowRoot, () -> {
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

        if(trans.size() < 1){
            trans_table.getChildren().add(new EmptyData(null, txt_keyword.getText()));
        }

        for(int i = 0; i < trans.size(); i++){
            TransactionEntity transaction = trans.get(i);
            TransactionColumn column = new TransactionColumn(shadowRoot, this::showTable, transaction);
            column.setBulkAction(this::toggleBulkItem);

            if(i % 2 == 1) column.getStyleClass().add("stripped");

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


}
