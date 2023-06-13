package siplaundry.view.cashier;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import siplaundry.controller.cashier.TransactionFormController;

import java.io.IOException;

public class TransactionFormView extends AnchorPane {
    public TransactionFormView(BorderPane parent_root, BorderPane shadow_root) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/cashier/transaction/form.fxml"));

        try {
            loader.setRoot(this);
            loader.setController(new TransactionFormController(parent_root, shadow_root));
            loader.load();
        }catch (IOException e) { e.printStackTrace(); }
    }
}