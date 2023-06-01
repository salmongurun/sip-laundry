package siplaundry.controller.admin;

import java.util.HashMap;
import java.util.*;
import java.util.List;

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
import siplaundry.entity.CustomerEntity;
import siplaundry.repository.CustomerRepo;
import siplaundry.view.admin.components.column.CustomerColumn;
import siplaundry.view.admin.components.modal.ConfirmDialog;
import siplaundry.view.admin.components.modal.CustomerModal;
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
    private ComboBox<String> CB_sortBy;
    @FXML
    private ComboBox<String> CB_column;

    private CustomerRepo custRepo = new CustomerRepo();

    private Set<CustomerEntity> bulkItems = new HashSet<>();

    private ArrayList<CustomerColumn> accColumns = new ArrayList<>();

    public CustomerController(BorderPane shadow){ this.shadowRoot = shadow;}

    @FXML
    void initialize(){
        ObservableList<String> items = FXCollections.observableArrayList(
            "A-Z",
            "Z-A"
        );
        CB_sortBy.setItems(items);

        ObservableList<String> column = FXCollections.observableArrayList(
            "Nama",
            "Alamat"
        );
        CB_column.setItems(column);
        
        List<CustomerEntity> customer = custRepo.get();

        showTable(customer);
        total_text.setText("Menampilkan total "+ customer.size());
    }

    @FXML
    void showAddCustomer(){
        new CustomerModal(shadowRoot, this::showTable, null);
    }

    @FXML
    void searchAction(KeyEvent event){
        String keyword = txt_keyword.getText();

        List<CustomerEntity> cust = custRepo.search(new HashMap<>(){{
            put("name", keyword);
            put("phone", keyword);
            put("address", keyword);
        }});

        showTable(cust);
    }

    @FXML
    void SortAction(){
        String SortBy = " DESC";
        String column = " name";
        
        if(CB_sortBy.getValue().equals("A-Z")) SortBy = " ASC";
        if(CB_column.getValue().equals("Alamat")) column = " address";

        List<CustomerEntity> cust = custRepo.sortBy(column, SortBy);
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

    void showTable(List<CustomerEntity> customer){
        customer_table.getChildren().clear();

        if(customer == null) customer = custRepo.get();
        if(customer.size() < 1) {
            customer_table.getChildren().add(new EmptyData(this::showAddCustomer, txt_keyword.getText()));
        }

        for(CustomerEntity cust : customer){
            // customer_table.getChildren().add(new CustomerColumn(shadowRoot, this::showTable, cust));
            
            CustomerColumn column = new CustomerColumn(shadowRoot, this::showTable, cust);
            column.setBulkAction(this::toggleBulkItem);

            customer_table.getChildren().add(column);
            accColumns.add(column);
        }
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
