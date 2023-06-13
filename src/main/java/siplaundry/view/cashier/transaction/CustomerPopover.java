package siplaundry.view.cashier.transaction;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import siplaundry.entity.CustomerEntity;
import siplaundry.repository.CustomerRepo;
import siplaundry.view.admin.components.modal.CustomerModal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class CustomerPopover extends AnchorPane {
    @FXML
    private TextField txt_keyword;
    @FXML
    private VBox customer_list;

    private Consumer<CustomerEntity> setCustomer;
    private final CustomerRepo custRepo = new CustomerRepo();
    private BorderPane shadow_root;
    public CustomerPopover(Consumer<CustomerEntity> setCustomer, BorderPane shadowRoot) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/transaction/customer-popover.fxml"));
        this.setCustomer = setCustomer;
        this.shadow_root = shadowRoot;

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

    @FXML
    void showAddCustomer() {
        new CustomerModal(this.shadow_root, this::showCustomers, null);
    }

    void showCustomers(List<CustomerEntity> customers) {
        if(customers == null) return;
        customer_list.getChildren().clear();

        for(CustomerEntity customer: customers) {
            customer_list.getChildren().add(new CustomerPopoverItem(
                customer,
                this.setCustomer
            ));
        }
    }
}
