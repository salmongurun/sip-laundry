package siplaundry.view.cashier.transaction;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import siplaundry.entity.CustomerEntity;

import java.io.IOException;

public class CustomerPopoverItem extends HBox {
    @FXML
    private Text customer_name, customer_phone;
    private CustomerEntity customer;

    public CustomerPopoverItem(CustomerEntity customer) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/transaction/customer-popover-item.fxml"));
        this.customer = customer;

        try {
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch(IOException e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {
        customer_name.setText(customer.getname());
        customer_phone.setText(customer.getphone());
    }
}
