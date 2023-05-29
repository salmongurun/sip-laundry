package siplaundry.controller.admin;

import java.util.HashMap;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import siplaundry.entity.CustomerEntity;
import siplaundry.repository.CustomerRepo;
import siplaundry.view.admin.components.column.CustomerColumn;
import siplaundry.view.admin.components.modal.CustomerModal;

public class CustomerController {

    private BorderPane shadowRoot;

    @FXML
    private HBox btn_add_customer;

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
    void showAddCustomer(MouseEvent event){
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

    void showTable(List<CustomerEntity> customer){
        customer_table.getChildren().clear();

        if(customer == null) customer = custRepo.get();
        for(CustomerEntity cust : customer){
            customer_table.getChildren().add(new CustomerColumn(shadowRoot, this::showTable, cust));
        }
    }
}
