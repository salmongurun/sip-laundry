package siplaundry.controller.admin;

import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import siplaundry.entity.CustomerEntity;
import siplaundry.repository.CustomerRepo;
import siplaundry.view.admin.components.column.CustomerColumn;

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

    private CustomerRepo custRepo = new CustomerRepo();

    public CustomerController(BorderPane shadow){ this.shadowRoot = shadow;}

    @FXML
    void initialize(){
        List<CustomerEntity> customer = custRepo.get();

        showTable(customer);
        total_text.setText("Menampilkan total "+ customer.size());
    }

    @FXML
    void searchAction(KeyEvent event){
        String keyword = txt_keyword.getText();

        List<CustomerEntity> cust = custRepo.search(new HashMap<>(){{
            put("name", keyword);
            put("phone", keyword);
        }});

        showTable(cust);
    }

    void showTable(List<CustomerEntity> customer){
        customer_table.getChildren().clear();

        for(CustomerEntity cust : customer){
            customer_table.getChildren().add(new CustomerColumn(shadowRoot, cust));
        }
    }
}
