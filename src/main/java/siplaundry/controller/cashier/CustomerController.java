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
import siplaundry.data.SortingOrder;
import siplaundry.entity.CustomerEntity;
import siplaundry.repository.CustomerRepo;
import siplaundry.util.ViewUtil;
import siplaundry.view.admin.components.modal.ConfirmDialog;
import siplaundry.view.admin.components.modal.CustomerModal;
import siplaundry.view.cashier.components.column.CustomerColumn;
import siplaundry.view.util.EmptyData;
import toast.Toast;
import toast.ToastType;

public class CustomerController {
    private BorderPane shadowRoot;
    @FXML
    private HBox btn_add_customer, btn_bulk_delete;
    @FXML
    private VBox customer_table;
    @FXML
    private Text total_text;
    @FXML
    private TextField txt_keyword;
    @FXML
    private FontIcon sort_icon;
    @FXML
    private ComboBox<String> CB_column;

    private CustomerRepo custRepo = new CustomerRepo();
    private Set<CustomerEntity> bulkItems = new HashSet<>();
    private ArrayList<CustomerColumn> accColumns = new ArrayList<>();

    private SortingOrder sortOrder = SortingOrder.ASC;
    public CustomerController(BorderPane shadow){ this.shadowRoot = shadow;}

     @FXML
    void initialize(){
        List<CustomerEntity> customer = custRepo.get();
        ObservableList<String> column = FXCollections.observableArrayList(
            "Nama",
            "Alamat"
        );

        CB_column.setItems(column);
        showTable(customer);
    }

    @FXML
    void showAddCustomer(){
        new CustomerModal(shadowRoot, this::showTable, null);
    }

    @FXML
    void searchAction(KeyEvent event){
        List<CustomerEntity> cust = custRepo.search(this.searchableValues());
        showTable(cust);
    }

    private HashMap<String, Object> searchableValues() {
        String keyword = txt_keyword.getText();
        return new HashMap<>() {{
            put("name", keyword);
            put("phone", keyword);
            put("address", keyword);
        }};
    }

    @FXML
    void sortAction(){
        String column = "name";

        this.sortOrder = ViewUtil.switchOrderIcon(this.sortOrder, this.sort_icon);

        if(CB_column.getValue() != null){
            if(CB_column.getValue().equals("Alamat")) column = " address";
        }

        List<CustomerEntity> cust = custRepo.search(this.searchableValues(), column, this.sortOrder);
        showTable(cust);
     }

     @FXML
     void selectBulkAll(){
        for(CustomerColumn column: accColumns){
            column.toggleBulk();
        }
     }

     @FXML
    void bulkDelete(){
        new ConfirmDialog(shadowRoot, () -> {
            for(CustomerEntity cust: this.bulkItems){
                custRepo.delete(cust.getid());
            }

            new Toast((AnchorPane) btn_bulk_delete.getScene().getRoot())
                .show(ToastType.SUCCESS, "Berhasil melakukan hapus semua", null);
            showTable(custRepo.get());
        });
    }

     void showTable(List<CustomerEntity> customers){
        customer_table.getChildren().clear();

        if(customers == null) customers = custRepo.get();

        if(customers.size() < 1) {
            customer_table.getChildren().add(new EmptyData(this::showAddCustomer, txt_keyword.getText()));
            return;
        }

        for(int i = 0; i < customers.size(); i++){
            CustomerEntity cust = customers.get(i);
            CustomerColumn column = new CustomerColumn(shadowRoot, this::showTable, cust);
            column.setBulkAction(this::toggleBulkItem);

            if(i % 2 == 1) column.getStyleClass().add("stripped");

            customer_table.getChildren().add(column);
            accColumns.add(column);
        }
        total_text.setText("Menampilkan total "+ customers.size());
    }

    protected void toggleBulkItem(CustomerEntity cust){
        if(this.bulkItems.contains(cust)){
            this.bulkItems.remove(cust);
        } else {
            this.bulkItems.add(cust);
        }

        btn_bulk_delete.setDisable(this.bulkItems.size() < 1);
    }
}
