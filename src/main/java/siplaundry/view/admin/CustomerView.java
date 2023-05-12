package siplaundry.view.admin;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import siplaundry.controller.admin.CustomerController;

public class CustomerView extends AnchorPane {
    public CustomerView(BorderPane shadow) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/admin/customer.fxml"));

        loader.setRoot(this);
        loader.setController(new CustomerController(shadow));
        loader.load();
    }
}
