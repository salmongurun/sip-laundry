package siplaundry.controller.cashier;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import siplaundry.view.cashier.TransactionFormView;

public class TransactionController {
    private BorderPane parent_root, shadow_root;
    public TransactionController(BorderPane parent_root, BorderPane shadow_root) {
        this.parent_root = parent_root;
        this.shadow_root = shadow_root;
    }

    @FXML
    void initialize() {

    }
    @FXML
    void showAddTransaction() {
        this.parent_root.setCenter(new TransactionFormView(parent_root, shadow_root));
    }
}
