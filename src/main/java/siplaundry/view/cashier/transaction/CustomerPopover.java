package siplaundry.view.cashier.transaction;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import siplaundry.entity.CustomerEntity;
import siplaundry.repository.CustomerRepo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomerPopover extends AnchorPane {
    @FXML
    private TextField txt_keyword;
    @FXML
    private VBox customer_list;

    private final CustomerRepo custRepo = new CustomerRepo();
    public CustomerPopover() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/transaction/customer-popover.fxml"));

        try {
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {
        showCustomers((ArrayList<CustomerEntity>) custRepo.get());
    }

    @FXML
    void searchAction() {
        showCustomers((ArrayList<CustomerEntity>) custRepo.search(new HashMap<>() {{
            put("name", txt_keyword.getText());
            put("phone", txt_keyword.getText());
        }}));
    }

    void showCustomers(ArrayList<CustomerEntity> customers) {
        customer_list.getChildren().clear();

        for(CustomerEntity customer: customers) {
            customer_list.getChildren().add(new CustomerPopoverItem(customer));
        }
    }
}
